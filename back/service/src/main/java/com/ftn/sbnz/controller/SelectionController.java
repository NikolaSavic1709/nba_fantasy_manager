package com.ftn.sbnz.controller;

import com.ftn.sbnz.DTO.players.PlayerDTO;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.service.SelectionService;
import com.ftn.sbnz.utils.KieSessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class SelectionController {

    private final KieSessionProvider kieSessionProvider;
    private final SelectionService selectionService;

    @Autowired
    public SelectionController(KieSessionProvider kieSessionProvider, SelectionService selectionService) {
        this.kieSessionProvider = kieSessionProvider;
        this.selectionService = selectionService;
    }

    @GetMapping("/selections/threshold")
    public ResponseEntity<?> getMostSelectedPlayersByThreshold(@RequestParam int threshold) {
        List<Player> players = selectionService.getMostSelectedPlayersByThreshold(threshold);
        List<PlayerDTO> playerDTOList = new ArrayList<>();
        for(Player p : players){
            playerDTOList.add(new PlayerDTO(p));
        }

        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
    }

    @GetMapping("/selections/team")
    public ResponseEntity<?> getMostSelectedPlayersByNBATeamName(@RequestParam String team) {
        List<Player> players = selectionService.getMostSelectedPlayersByNBATeamName(team);
        List<PlayerDTO> playerDTOList = new ArrayList<>();
        for(Player p : players){
            playerDTOList.add(new PlayerDTO(p));
        }

        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
    }

    @GetMapping("/dropped/threshold")
    public ResponseEntity<?> getMostDroppedPlayersByThreshold(@RequestParam int threshold) {
        List<Player> players = selectionService.getMostDroppedPlayersByThreshold(threshold);
        List<PlayerDTO> playerDTOList = new ArrayList<>();
        for(Player p : players){
            playerDTOList.add(new PlayerDTO(p));
        }

        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
    }

    @GetMapping("/dropped/team")
    public ResponseEntity<?> getMostDroppedPlayersByNBATeamName(@RequestParam String team) {
        List<Player> players = selectionService.getMostDroppedPlayersByNBATeamName(team);
        List<PlayerDTO> playerDTOList = new ArrayList<>();
        for(Player p : players){
            playerDTOList.add(new PlayerDTO(p));
        }

        return new ResponseEntity<>(playerDTOList, HttpStatus.OK);
    }

    @GetMapping("/selections/teams-with-selected-players")
    public ResponseEntity<?> getTeamsWithSelectedPlayers() {
        return new ResponseEntity<>(selectionService.getTeamsWithSelectedPlayers(), HttpStatus.OK);
    }


}
