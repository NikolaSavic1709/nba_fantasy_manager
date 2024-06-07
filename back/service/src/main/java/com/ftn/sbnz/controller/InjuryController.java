package com.ftn.sbnz.controller;

import com.ftn.sbnz.DTO.players.PlayerDTO;
import com.ftn.sbnz.DTO.stats.InjuryStatsDTO;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.service.InjuryService;
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
@RequestMapping(value = "/api/injury")
public class InjuryController {

    private final KieSessionProvider kieSessionProvider;
    private final InjuryService injuryService;

    @Autowired
    public InjuryController(KieSessionProvider kieSessionProvider, InjuryService injuryService) {
        this.kieSessionProvider = kieSessionProvider;
        this.injuryService = injuryService;
    }

    @GetMapping("/threshold")
    public ResponseEntity<?> getMostSelectedPlayersByThreshold(@RequestParam int threshold) {
        List<InjuryStatsDTO> injuries = injuryService.getMostFrequentInjuryByThreshold(threshold);
        return new ResponseEntity<>(injuries, HttpStatus.OK);
    }
}
