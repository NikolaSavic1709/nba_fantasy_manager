package com.ftn.sbnz.controller;

import com.ftn.sbnz.model.dto.InjuryDTO;
import com.ftn.sbnz.model.dto.NewInjuryDTO;
import com.ftn.sbnz.model.dto.PlayerBasicInfoDTO;
import com.ftn.sbnz.model.dto.PlayerDetailsDTO;
import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.model.models.stats.CategoryScores;
import com.ftn.sbnz.repository.players.IInjuryRepository;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import com.ftn.sbnz.utils.KieSessionProvider;
import com.ftn.sbnz.utils.TokenUtils;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.ObjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import com.ftn.sbnz.model.models.injuries.Injury;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PlayerStatus;

@RestController
@RequestMapping(value = "/api")
public class SampleHomeController {

	private final KieContainer kieContainer;

	private final TokenUtils tokenUtils;
	private final KieSessionProvider kieSessionProvider;
	private final IPlayerRepository playerRepository;
	private final IInjuryRepository injuryRepository;

	@Autowired
    public SampleHomeController(KieContainer kieContainer, TokenUtils tokenUtils, KieSessionProvider kieSessionProvider, IPlayerRepository playerRepository, IInjuryRepository injuryRepository) {
        this.kieContainer = kieContainer;
		this.tokenUtils = tokenUtils;
		this.kieSessionProvider = kieSessionProvider;
        this.playerRepository = playerRepository;
		this.injuryRepository = injuryRepository;
	}

    @PostMapping("/injury")
	public ResponseEntity<?> injury(@RequestBody NewInjuryDTO newInjuryDTO) {

		Optional<Player> p = playerRepository.findById(newInjuryDTO.getPlayerId()); //Lebron 1, Curry 5
		if(p.isPresent()){
			Injury i = new Injury();
			i.setDescription(newInjuryDTO.getDescription());
			i.setName(newInjuryDTO.getName());
			i.setRecovered(false);
			i.setTimestamp(new Date());
			i.setPlayer(p.get());
			injuryRepository.save(i);
			this.kieSessionProvider.getKieSession().insert(i);
			this.kieSessionProvider.getKieSession().fireAllRules();
			return ResponseEntity.ok(new InjuryDTO(i));
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");

	}

	@PutMapping("/recovery/{id}")
	public ResponseEntity<?> recovery(@PathVariable("id") int id, @RequestHeader("Authorization") String authHeader) {

		Optional<Injury> i= injuryRepository.findById(id);
		if(i.isPresent()){
			i.get().setRecovered(true);
			injuryRepository.save(i.get());

			FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(i);

			if (factHandle != null) {
				kieSessionProvider.getKieSession().update(factHandle, i);
			} else {
				kieSessionProvider.getKieSession().insert(i);
			}

			kieSessionProvider.getKieSession().fireAllRules();
			return ResponseEntity.noContent().build();
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Injury not found");

	}
	@GetMapping("/currentInjuries")
	public ResponseEntity<?> getCurrentInjuries() throws Exception {
		List<Injury> injuries=injuryRepository.findAll();
		List<InjuryDTO> injuriesResponse=injuries.stream()
				.filter(injury -> !injury.isRecovered())
				.map(InjuryDTO::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(injuriesResponse);
	}

//	@RequestMapping(value = "/filter", method = RequestMethod.GET, produces = "application/json")
//	public Filter filter(@RequestParam(required = true) Integer minPrice, @RequestParam(required = true) Integer maxPrice,
//						 @RequestParam(required = true) String team, @RequestParam(required = true) Integer position) {
//
//		//kieSessionProvider.getKieSession().getAgenda().getAgendaGroup("recommendation-group").setFocus();
//		Filter filter = new Filter(1L, minPrice, maxPrice, team, position);
//		NBATeam team1 = new NBATeam(1L, null, "Denver");
//		NBATeam team2 = new NBATeam(1L, null, "Lakers");
//
//		Player p1 = new Player();
//		p1.setName("Jamal");
//		p1.setStatus(PlayerStatus.HEALTHY);
//		p1.setPrice(20);
//		p1.setNbaTeam(team1);
//		p1.setPosition(Arrays.asList(1, 2, 3));
//
//		Player p2 = new Player();
//		p2.setName("Anthony");
//		p2.setStatus(PlayerStatus.HEALTHY);
//		p2.setPrice(50);
//		p2.setNbaTeam(team2);
//		p2.setPosition(Arrays.asList(4, 5));
//
//		kieSessionProvider.getKieSession().insert(filter);
//		kieSessionProvider.getKieSession().insert(p1);
//		kieSessionProvider.getKieSession().insert(p2);
//		kieSessionProvider.getKieSession().fireAllRules();
//		return filter;
//	}

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
		//kieSessionProvider.getKieSession().getAgenda().getAgendaGroup("recommendation-group").setFocus();
		Collection<?> recommendationLists = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				return object instanceof RecommendationList;
			}
		});

		if (recommendationLists.isEmpty()) {
			kieSessionProvider.getKieSession().insert(categoryScores);
			kieSessionProvider.getKieSession().insert(rl);

			kieSessionProvider.getKieSession().fireAllRules();
		} else {
			rl = (RecommendationList) recommendationLists.iterator().next();

		}
		return rl;
	}

	@RequestMapping("/style")
	public String style() {
		Player p = playerRepository.findByName("Jayson Tatum").orElse(null);

		for (Object obj : this.kieSessionProvider.getKieSession().getObjects()) {
			if (obj instanceof Player) {
				p = (Player) obj;
				if (p.getName().equals("LeBron James")) {
					break;
				}
			}
		}
		FactHandle handle = this.kieSessionProvider.getKieSession().getFactHandle(p);

		p.setStatus(PlayerStatus.OUT);
		this.kieSessionProvider.getKieSession().update(handle,p);
		this.kieSessionProvider.getKieSession().fireAllRules();
		return "style";
	}

	
}
