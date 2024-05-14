package com.ftn.sbnz.service;

import com.ftn.sbnz.model.events.Item;
import com.ftn.sbnz.model.models.*;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@RestController
public class SampleHomeController {

	private final KieContainer kieContainer;

	@Autowired
    public SampleHomeController(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @RequestMapping("/injury")
	public String injury() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kContainer = ks.getKieClasspathContainer();
//		KieSession kieSession = kContainer.newKieSession("fwKsession");
		KieSession kieSession = kieContainer.newKieSession("fwKsession");
		Player p = new Player();
		p.setId(1L);
		p.setStatus(PlayerStatus.HEALTHY);
		Injury i = new Injury(1L,"name", "desc", false, null, null, new Date(), p);
		kieSession.insert(i);
		kieSession.insert(p);
		kieSession.fireAllRules();
		kieSession.dispose();
		return "inj";
	}

	@RequestMapping("/recovery")
	public String recovery() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kContainer = ks.getKieClasspathContainer();
//		KieSession kieSession = kContainer.newKieSession("fwKsession");
		KieSession kieSession = kieContainer.newKieSession("fwKsession");
		Player p = new Player();
		p.setId(1L);
		p.setStatus(PlayerStatus.OUT);
		Calendar myCal = Calendar.getInstance();
		myCal.set(2024,Calendar.APRIL,20);
		Date date = myCal.getTime();
		Injury i = new Injury(1L,"name", "desc", true, null, null, date, p);
		kieSession.insert(i);
		kieSession.insert(p);
		kieSession.fireAllRules();
		kieSession.dispose();
		return "rec";
	}

	@RequestMapping(value = "/filter", method = RequestMethod.GET, produces = "application/json")
	public Filter filter(@RequestParam(required = true) Integer minPrice, @RequestParam(required = true) Integer maxPrice,
						 @RequestParam(required = true) String team, @RequestParam(required = true) Integer position) {

		KieSession kieSession = kieContainer.newKieSession("bwKsession");
		Filter filter = new Filter(minPrice, maxPrice, team, position, null);
		NBATeam team1 = new NBATeam(1L, null, "Denver");
		NBATeam team2 = new NBATeam(1L, null, "Lakers");

		Player p1 = new Player();
		p1.setName("Jamal");
		p1.setStatus(PlayerStatus.HEALTHY);
		p1.setPrice(20);
		p1.setNbaTeam(team1);
		p1.setPosition(Arrays.asList(1, 2, 3));

		Player p2 = new Player();
		p2.setName("Anthony");
		p2.setStatus(PlayerStatus.HEALTHY);
		p2.setPrice(50);
		p2.setNbaTeam(team2);
		p2.setPosition(Arrays.asList(4, 5));

		kieSession.insert(filter);
		kieSession.insert(p1);
		kieSession.insert(p2);
		kieSession.fireAllRules();
		kieSession.dispose();
		return filter;
	}

	@RequestMapping(value = "/recommend", method = RequestMethod.GET, produces = "application/json")
	public RecommendationList recommendationList() {

		KieSession kieSession = kieContainer.newKieSession("fwKsession");

		CategoryScores categoryScores = new CategoryScores();
		categoryScores.setPointScore(1);
		categoryScores.setAssistScore(2);
		categoryScores.setBlockScore(3);
		categoryScores.setReboundScore(1);
		categoryScores.setStealScore(3);
		categoryScores.setTurnoverScore(-1);

		StatisticalColumns stats1 = new StatisticalColumns();
		stats1.setPpg(10);
		stats1.setApg(2);
		stats1.setRpg(5);
		stats1.setSpg(1);
		stats1.setTpg(2);
		stats1.setBpg(1);
		stats1.setGp(1);

		StatisticalColumns stats2 = new StatisticalColumns();
		stats2.setPpg(0);
		stats2.setApg(5);
		stats2.setRpg(10);
		stats2.setSpg(0);
		stats2.setTpg(5);
		stats2.setBpg(0);
		stats2.setGp(2);

		Player p1 = new Player();
		p1.setName("Jamal");
		p1.setStatisticalColumns(stats1);

		Player p2 = new Player();
		p2.setName("Anthony");
		p2.setStatisticalColumns(stats2);

		RecommendationList rl = new RecommendationList();

		kieSession.insert(categoryScores);
		kieSession.insert(p1);
		kieSession.insert(p2);
		kieSession.insert(rl);
		kieSession.fireAllRules();
		kieSession.dispose();
		return rl;
	}
}
