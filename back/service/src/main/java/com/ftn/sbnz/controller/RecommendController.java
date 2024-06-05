package com.ftn.sbnz.controller;

import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.RecommendationList;
import com.ftn.sbnz.model.models.StartFilter;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import com.ftn.sbnz.utils.KieSessionProvider;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class RecommendController {

    private final KieSessionProvider kieSessionProvider;
    private final IPlayerRepository playerRepository;

    @Autowired
    public RecommendController(KieContainer kieContainer, KieSessionProvider kieSessionProvider, IPlayerRepository playerRepository) {
        this.kieSessionProvider = kieSessionProvider;
        this.playerRepository = playerRepository;
    }

    @GetMapping(value = "/recommendation_list")
    public String getRecommendationList() {
        RecommendationList rl = new RecommendationList();
        kieSessionProvider.getKieSession().insert(rl);
        int i = kieSessionProvider.getKieSession().fireAllRules();
        System.out.println(i);
        for(Player p : rl.getPlayers()){
            System.out.println("Player: " + p.getName() + " have: " + p.getTotalFantasyPoints());
        }

        return "recommendation list";
    }
}
