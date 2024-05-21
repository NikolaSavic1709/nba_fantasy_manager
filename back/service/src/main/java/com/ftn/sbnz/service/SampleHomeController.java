package com.ftn.sbnz.service;

import com.ftn.sbnz.model.events.Item;
import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import com.ftn.sbnz.model.models.Injury;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PlayerStatus;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
public class SampleHomeController {

	private final KieContainer kieContainer;

	private final KieSession kieSession;

	private final IPlayerRepository playerRepository;

	@Autowired
    public SampleHomeController(KieContainer kieContainer, KieSession kieSession, IPlayerRepository playerRepository) {
        this.kieContainer = kieContainer;
		this.kieSession = kieSession;
        this.playerRepository = playerRepository;
    }

    @RequestMapping("/injury_1")
	public String injury_1() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kContainer = ks.getKieClasspathContainer();
//		KieSession kieSession = kContainer.newKieSession("fwKsession");
//		KieSession kieSession = kieContainer.newKieSession("fwKsession");
		System.out.println("controller");
		System.out.println(this.kieSession);
		Player p = new Player();
		p.setId(1L);
		p.setStatus(PlayerStatus.HEALTHY);
		Injury i = new Injury(1L,"name", "placed on IL with strained right hip flexor", false, null, null, new Date(), p);
		this.kieSession.insert(i);
		this.kieSession.insert(p);
		this.kieSession.fireAllRules();
//		this.kieSession.dispose();
		return i.getEstimatedRecoveryTimeInDays().toString();
	}
	@RequestMapping("/injury")
	public String injury() {
		CategoryScores categoryScores=new CategoryScores();
		categoryScores.setBonusMargin(-10);
		categoryScores.setAssistScore(1);
		categoryScores.setBlockScore(1);
		categoryScores.setReboundScore(1);
		categoryScores.setPointScore(1);
		categoryScores.setStealScore(1);
		categoryScores.setTurnoverScore(1);

		NBATeam denver=new NBATeam();
		NBATeam minnesota=new NBATeam();

		Player jokic = new Player();
		Player murray = new Player();
		Player gordon = new Player();
		Player edwards = new Player();
		{
			denver.setName("Denver");
			denver.setPlayers(new ArrayList<>());
			minnesota.setName("Minnesota");
			minnesota.setPlayers(new ArrayList<>());

			jokic.setId(1L);
			jokic.setName("Nikola Jokic");
			jokic.setStatus(PlayerStatus.HEALTHY);
			jokic.setTotalBonusPoints(0);
			StatisticalColumns jokiceveKolone = new StatisticalColumns();
			jokiceveKolone.setGp(40);
			jokic.setStatisticalColumns(jokiceveKolone);
			jokic.setNbaTeam(denver);


			murray.setId(2L);
			murray.setName("Jamal Murray");
			murray.setStatus(PlayerStatus.HEALTHY);
			murray.setTotalBonusPoints(0);
			StatisticalColumns marijeveKolone = new StatisticalColumns();
			marijeveKolone.setGp(30);
			murray.setStatisticalColumns(marijeveKolone);
			murray.setNbaTeam(denver);

			gordon.setId(3L);
			gordon.setName("Aaron Gordon");
			gordon.setStatus(PlayerStatus.HEALTHY);
			gordon.setTotalBonusPoints(0);
			StatisticalColumns gordonoveKolone = new StatisticalColumns();
			gordonoveKolone.setGp(25);
			gordon.setStatisticalColumns(gordonoveKolone);
			gordon.setNbaTeam(denver);

			edwards.setId(4L);
			edwards.setName("Anthony Edwards");
			edwards.setStatus(PlayerStatus.HEALTHY);
			edwards.setTotalBonusPoints(0);
			StatisticalColumns edvardsoveKolone = new StatisticalColumns();
			edvardsoveKolone.setGp(50);
			edwards.setStatisticalColumns(edvardsoveKolone);
			edwards.setNbaTeam(minnesota);
		}
		minnesota.getPlayers().add(edwards);
		denver.getPlayers().add(jokic);
		denver.getPlayers().add(murray);
		denver.getPlayers().add(gordon);
		Injury i = new Injury(1L,"name", "placed on IL with strained right hip flexor", false, null, null, new Date(), gordon);
		this.kieSession.insert(i);

		this.kieSession.insert(jokic);
		this.kieSession.insert(murray);
		this.kieSession.insert(gordon);
		this.kieSession.insert(edwards);
		this.kieSession.insert(denver);
		this.kieSession.insert(minnesota);
		this.kieSession.insert(categoryScores);

		this.kieSession.fireAllRules();
//		this.kieSession.dispose();
		return i.getEstimatedRecoveryTimeInDays().toString();
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

	@RequestMapping("/style")
	public String style() {
		KieSession kieSession = kieContainer.newKieSession("bwKsession");
		Player p = new Player();
		p.setName("Miki");
		p.setStatus(PlayerStatus.OUT);

		Player p2 = playerRepository.findByName("Kevin Durant").orElse(null);
		//Player p2 = playerRepository.findByName("Denzel Valentine").orElse(null);
		p.setPlayerStyle(p2);
		kieSession.insert(p);
		kieSession.fireAllRules();
		return "style";
	}

	
}
