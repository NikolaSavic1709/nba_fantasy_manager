package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.ObjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import com.ftn.sbnz.model.models.Injury;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PlayerStatus;

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

    @RequestMapping("/injury")
	public String injury() {

		Player p = playerRepository.getReferenceById(1); //Lebron 1, Curry 5

		Injury i = new Injury();
		i.setDescription("placed on IL with strained right hip flexor");
		i.setRecovered(false);
		i.setTimestamp(new Date());
		i.setPlayer(p);

		//kieSession.getAgenda().getAgendaGroup("injury-group").setFocus();
		this.kieSession.insert(i);
		this.kieSession.fireAllRules();
//		this.kieSession.dispose();
		return i.getEstimatedRecoveryTimeInDays().toString();
	}
	@RequestMapping("/injury_1")
	public String injury_1() {
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

		//kieSession.getAgenda().getAgendaGroup("injury-group").setFocus();
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
		Player p = new Player();
		p.setId(1L);
		p.setStatus(PlayerStatus.OUT);
		Calendar myCal = Calendar.getInstance();
		myCal.set(2024,Calendar.APRIL,20);
		Date date = myCal.getTime();
		Injury i = new Injury(1L,"name", "desc", true, null, null, date, p);

		//kieSession.getAgenda().getAgendaGroup("injury-group").setFocus();
		kieSession.insert(i);
		kieSession.insert(p);
		kieSession.fireAllRules();
		return "rec";
	}


	@RequestMapping(value = "/filter", method = RequestMethod.GET, produces = "application/json")
	public Filter filter(@RequestParam(required = true) Integer minPrice, @RequestParam(required = true) Integer maxPrice,
						 @RequestParam(required = true) String team, @RequestParam(required = true) Integer position) {

		//kieSession.getAgenda().getAgendaGroup("recommendation-group").setFocus();
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
		return filter;
	}

	@RequestMapping(value = "/recommend", method = RequestMethod.GET, produces = "application/json")
	public RecommendationList recommendationList() {


		CategoryScores categoryScores = new CategoryScores();
		categoryScores.setBonusMargin(-10);
		categoryScores.setPointScore(1);
		categoryScores.setAssistScore(2);
		categoryScores.setBlockScore(3);
		categoryScores.setReboundScore(1);
		categoryScores.setStealScore(3);
		categoryScores.setTurnoverScore(-1);


		RecommendationList rl = new RecommendationList();
		KieServices ks = KieServices.Factory.get();
		//kieSession.getAgenda().getAgendaGroup("recommendation-group").setFocus();
		Collection<?> recommendationLists = kieSession.getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				return object instanceof RecommendationList;
			}
		});

		if (recommendationLists.isEmpty()) {
			kieSession.insert(categoryScores);
			kieSession.insert(rl);

			kieSession.fireAllRules();
		} else {
			rl = (RecommendationList) recommendationLists.iterator().next();

		}
		return rl;
	}

	@RequestMapping("/style")
	public String style() {
		Player p = playerRepository.findByName("Jayson Tatum").orElse(null);

		for (Object obj : this.kieSession.getObjects()) {
			if (obj instanceof Player) {
				p = (Player) obj;
				if (p.getName().equals("LeBron James")) {
					break;
				}
			}
		}
		FactHandle handle = this.kieSession.getFactHandle(p);

		p.setStatus(PlayerStatus.OUT);
		this.kieSession.update(handle,p);
		this.kieSession.fireAllRules();
		return "style";
	}

	
}
