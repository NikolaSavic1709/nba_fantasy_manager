package com.ftn.sbnz.controller;

import com.ftn.sbnz.model.dto.PlayerBasicInfoDTO;
import com.ftn.sbnz.model.dto.PlayerDetailsDTO;
import com.ftn.sbnz.model.dto.PlayerShortInfoDTO;
import com.ftn.sbnz.model.events.PlayerAdditionEvent;
import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.RecommendationList;
import com.ftn.sbnz.model.models.user.Manager;
import com.ftn.sbnz.model.repository.IFantasyTeamRepository;
import com.ftn.sbnz.model.repository.players.IPlayerRepository;
import com.ftn.sbnz.model.repository.users.IManagerRepository;
import com.ftn.sbnz.service.UserService;
import com.ftn.sbnz.utils.KieSessionProvider;
import com.ftn.sbnz.utils.TokenUtils;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class PlayerController {

    private final TokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final IManagerRepository managerRepository;
    private final IPlayerRepository playerRepository;
    private final IFantasyTeamRepository fantasyTeamRepository;
    private final KieSessionProvider kieSessionProvider;

    public PlayerController(TokenUtils tokenUtils, AuthenticationManager authenticationManager, UserService userService, IManagerRepository managerRepository, IPlayerRepository playerRepository, IFantasyTeamRepository fantasyTeamRepository, KieSessionProvider kieSessionProvider) {
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.managerRepository = managerRepository;
        this.playerRepository = playerRepository;
        this.fantasyTeamRepository = fantasyTeamRepository;
        this.kieSessionProvider = kieSessionProvider;
    }

    @GetMapping("/playerDetails/{id}")
    public ResponseEntity<?> getPlayerDetails(@PathVariable("id") Long id) throws Exception {
        Optional<Player> player = playerRepository.findById(id);
        if (player.isPresent())
            return ResponseEntity.ok(new PlayerDetailsDTO(player.get()));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");

    }

    @GetMapping("/fantasyTeamPlayers/{id}")
    public ResponseEntity<?> getPlayersFromFantasyTeam(@PathVariable("id") int id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = tokenUtils.getIdFromToken(token);
        if (id == userId) {
            Optional<Manager> manager = managerRepository.findById(id);
            if (manager.isPresent()) {
                List<Player> players = manager.get().getTeam().getPlayers();
                List<PlayerShortInfoDTO> playersResponse = players.stream()
                        .map(PlayerShortInfoDTO::new)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(playersResponse);
            } else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");

    }

    @GetMapping("/playersBasic")
    public ResponseEntity<?> getPlayersForAutocomplete() {

        List<Player> players = playerRepository.findAll();
        List<PlayerBasicInfoDTO> playersResponse = players.stream()
                .map(PlayerBasicInfoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playersResponse);


    }

    @PutMapping("/addPlayer/{playerId}")
    public ResponseEntity<?> addPlayer(@PathVariable("playerId") Long playerId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = tokenUtils.getIdFromToken(token);

        Optional<Manager> manager = managerRepository.findById(userId);
        if (manager.isPresent()) {
            Collection<?> players = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
                @Override
                public boolean accept(Object object) {
                    return object instanceof Player;
                }
            });
            Player player=null;
            Long teamId = manager.get().getTeam().getId();
            if (players.isEmpty()) {
                Optional<Player> playerOptional = playerRepository.findById(playerId);
                if (playerOptional.isPresent() && playerOptional.get().getFantasyTeam()==null) {
                    player = playerOptional.get();
                    player.setFantasyTeam(manager.get().getTeam());
                    playerRepository.save(player);
                    kieSessionProvider.getKieSession().insert(player);

                }
            } else {
                Player foundPlayer=null;
                for (Object player1 : players) {
                    if(Objects.equals(((Player) player1).getId(), playerId))
                        foundPlayer=(Player)player1;

                }
                if (foundPlayer==null) {
                    Optional<Player> playerOptional = playerRepository.findById(playerId);
                    if (playerOptional.isPresent() && playerOptional.get().getFantasyTeam()==null) {
                        player = playerOptional.get();
                        player.setFantasyTeam(manager.get().getTeam());
                        playerRepository.save(player);
                        kieSessionProvider.getKieSession().insert(player);

                    }
                }
                else {
                    player=foundPlayer;
                    player.setFantasyTeam(manager.get().getTeam());
                    playerRepository.save(player);
                    FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(player);

                    if (factHandle != null) {
                        kieSessionProvider.getKieSession().update(factHandle, player);
                    } else {
                        kieSessionProvider.getKieSession().insert(player);
                    }
                }

            }

            Collection<?> teams = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
                @Override
                public boolean accept(Object object) {
                    return object instanceof FantasyTeam;
                }
            });
            FantasyTeam team =null;
            if (teams.isEmpty()) {

                team=fantasyTeamRepository.findById(Math.toIntExact(teamId)).get();
                kieSessionProvider.getKieSession().insert(team);
            }
            else{
                FantasyTeam foundTeam=null;
                for (Object team1 : teams) {
                    if(Objects.equals(((FantasyTeam) team1).getId(), Long.valueOf(teamId)))
                        foundTeam=(FantasyTeam) team1;

                }
                if(foundTeam==null){

                    team=fantasyTeamRepository.findById(Math.toIntExact(teamId)).get();
                    kieSessionProvider.getKieSession().insert(team);
                }
                else{
                    team=foundTeam;
                    FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(team);

                    if (factHandle != null) {
                        kieSessionProvider.getKieSession().update(factHandle, team);
                    } else {
                        kieSessionProvider.getKieSession().insert(team);
                    }
                }
            }


            if(player!=null){
                PlayerAdditionEvent playerAdditionEvent=new PlayerAdditionEvent(new Date(),player, team);
                kieSessionProvider.getKieSession().insert(playerAdditionEvent);
            }
            kieSessionProvider.getKieSession().fireAllRules();
            return ResponseEntity.ok(new PlayerDetailsDTO(player));


        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");

    }
    @PutMapping("/removePlayer/{id}")
    public ResponseEntity<?> removePlayer(@PathVariable("id") Long id){

        Collection<?> players = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object object) {
                return object instanceof Player;
            }
        });
        Player player=null;
        Integer teamId = null;
        if (players.isEmpty()) {
            Optional<Player> playerOptional=playerRepository.findById(id);
            if (playerOptional.isPresent()) {
                player = playerOptional.get();
                teamId= Math.toIntExact(player.getFantasyTeam().getId());
                player.setFantasyTeam(null);
                playerRepository.save(player);
                kieSessionProvider.getKieSession().insert(player);
            }
        }
        else{
            Player foundPlayer=null;
            for (Object player1 : players) {
                if(Objects.equals(((Player) player1).getId(), id))
                    foundPlayer=(Player)player1;

            }
            if(foundPlayer==null){
                Optional<Player> playerOptional=playerRepository.findById(id);
                if (playerOptional.isPresent()) {
                    player = playerOptional.get();
                    teamId= Math.toIntExact(player.getFantasyTeam().getId());
                    player.setFantasyTeam(null);
                    playerRepository.save(player);
                    kieSessionProvider.getKieSession().insert(player);
                }
            }
            else{
                player=foundPlayer;
                teamId= Math.toIntExact(player.getFantasyTeam().getId());
                player.setFantasyTeam(null);
                playerRepository.save(player);
                FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(player);

                if (factHandle != null) {
                    kieSessionProvider.getKieSession().update(factHandle, player);
                } else {
                    kieSessionProvider.getKieSession().insert(player);
                }
            }
        }

        Collection<?> teams = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object object) {
                return object instanceof FantasyTeam;
            }
        });
        FantasyTeam team =null;
        if (teams.isEmpty()) {

            team=fantasyTeamRepository.findById(teamId).get();
            kieSessionProvider.getKieSession().insert(team);
        }
        else{
            FantasyTeam foundTeam=null;
            for (Object team1 : teams) {
                if(Objects.equals(((FantasyTeam) team1).getId(), Long.valueOf(teamId)))
                    foundTeam=(FantasyTeam) team1;

            }
            if(foundTeam==null){

                team=fantasyTeamRepository.findById(teamId).get();
                kieSessionProvider.getKieSession().insert(team);
            }
            else{
                team=foundTeam;
                FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(team);

                if (factHandle != null) {
                    kieSessionProvider.getKieSession().update(factHandle, team);
                } else {
                    kieSessionProvider.getKieSession().insert(team);
                }
            }
        }
        kieSessionProvider.getKieSession().fireAllRules();
        return ResponseEntity.noContent().build();


    }
}


