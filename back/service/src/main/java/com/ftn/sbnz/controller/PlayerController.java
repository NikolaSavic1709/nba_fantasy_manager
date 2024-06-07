package com.ftn.sbnz.controller;

import com.ftn.sbnz.model.dto.PlayerBasicInfoDTO;
import com.ftn.sbnz.model.dto.PlayerDetailsDTO;
import com.ftn.sbnz.model.dto.PlayerShortInfoDTO;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.user.Manager;
import com.ftn.sbnz.model.repository.players.IPlayerRepository;
import com.ftn.sbnz.model.repository.users.IManagerRepository;
import com.ftn.sbnz.service.UserService;
import com.ftn.sbnz.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class PlayerController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @Autowired
    private IManagerRepository managerRepository;
    @Autowired
    private IPlayerRepository playerRepository;

    @GetMapping("/playerDetails/{id}")
    public ResponseEntity<?> getPlayerDetails(@PathVariable("id") Long id) throws Exception {
        Optional<Player> player=playerRepository.findById(id);
        if (player.isPresent())
            return ResponseEntity.ok(new PlayerDetailsDTO(player.get()));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");

    }
    @GetMapping("/fantasyTeamPlayers/{id}")
    public ResponseEntity<?> getPlayersFromFantasyTeam(@PathVariable("id") int id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId=tokenUtils.getIdFromToken(token);
        if (id==userId){
            Optional<Manager> manager=managerRepository.findById(id);
            if(manager.isPresent())
            {
                List<Player> players=manager.get().getTeam().getPlayers();
                List<PlayerShortInfoDTO> playersResponse=players.stream()
                        .map(PlayerShortInfoDTO::new)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(playersResponse);
            }
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");

    }
    @GetMapping("/playersBasic")
    public ResponseEntity<?> getPlayersForAutocomplete() {

        List<Player> players=playerRepository.findAll();
        List<PlayerBasicInfoDTO> playersResponse=players.stream()
                .map(PlayerBasicInfoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(playersResponse);


    }
}
