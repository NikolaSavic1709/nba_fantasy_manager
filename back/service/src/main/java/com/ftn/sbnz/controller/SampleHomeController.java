package com.ftn.sbnz.controller;

import com.ftn.sbnz.model.dto.InjuryDTO;
import com.ftn.sbnz.model.dto.NewInjuryDTO;
import com.ftn.sbnz.model.dto.RecommendationListDTO;
import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.model.models.user.Manager;
import com.ftn.sbnz.model.repository.players.IInjuryRepository;
import com.ftn.sbnz.model.repository.players.IPlayerRepository;
import com.ftn.sbnz.model.repository.users.IManagerRepository;
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
	private final IManagerRepository managerRepository;

	@Autowired
    public SampleHomeController(KieContainer kieContainer, TokenUtils tokenUtils, KieSessionProvider kieSessionProvider, IPlayerRepository playerRepository, IInjuryRepository injuryRepository, IManagerRepository managerRepository) {
        this.kieContainer = kieContainer;
		this.tokenUtils = tokenUtils;
		this.kieSessionProvider = kieSessionProvider;
        this.playerRepository = playerRepository;
		this.injuryRepository = injuryRepository;
		this.managerRepository = managerRepository;
	}

    @PostMapping("/injury")
	public ResponseEntity<?> injury(@RequestBody NewInjuryDTO newInjuryDTO) {
		Long playerId = newInjuryDTO.getPlayerId();
		Collection<?> players = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				return object instanceof Player;
			}
		});
		Player player = null;
		if (players.isEmpty()) {
			Optional<Player> playerOptional = playerRepository.findById(playerId);
			if (playerOptional.isPresent()) {
				player = playerOptional.get();
				kieSessionProvider.getKieSession().insert(player);

			}
		} else {
			Player foundPlayer = null;
			for (Object player1 : players) {
				if (Objects.equals(((Player) player1).getId(), playerId))
					foundPlayer = (Player) player1;

			}
			if (foundPlayer == null) {
				Optional<Player> playerOptional = playerRepository.findById(playerId);
				if (playerOptional.isPresent()) {
					player = playerOptional.get();
					kieSessionProvider.getKieSession().insert(player);
				}
			}else {
					player = foundPlayer;
					FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(player);

					if (factHandle != null) {
						kieSessionProvider.getKieSession().update(factHandle, player);
					} else {
						kieSessionProvider.getKieSession().insert(player);
					}
			}

		}
		Injury i = new Injury();
		i.setDescription(newInjuryDTO.getDescription());
		i.setName(newInjuryDTO.getName());
		i.setRecovered(false);
		i.setTimestamp(new Date());
		i.setPlayer(player);
		injuryRepository.save(i);
		this.kieSessionProvider.getKieSession().insert(i);
		this.kieSessionProvider.getKieSession().fireAllRules();
		return ResponseEntity.ok(new InjuryDTO(i));
	}

	@PutMapping("/recovery/{id}")
	public ResponseEntity<?> recovery(@PathVariable("id") int id, @RequestHeader("Authorization") String authHeader) {
		Collection<?> injuries = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				return object instanceof Injury;
			}
		});
		Injury injury=null;
		if (injuries.isEmpty()) {
			Optional<Injury> injuryOptional = injuryRepository.findById(id);
			if (injuryOptional.isPresent()) {
				injury = injuryOptional.get();
				injury.setRecovered(true);
				injuryRepository.save(injury);
				kieSessionProvider.getKieSession().insert(injury);

			}
		} else {
			Injury foundInjury=null;
			for (Object injury1 : injuries) {
				if(Objects.equals(((Injury) injury1).getId(), Long.valueOf(id)))
					foundInjury=(Injury)injury1;

			}
			if (foundInjury==null) {
				Optional<Injury> injuryOptional = injuryRepository.findById(id);
				if (injuryOptional.isPresent()) {
					injury = injuryOptional.get();
					injury.setRecovered(true);
					injuryRepository.save(injury);
					kieSessionProvider.getKieSession().insert(injury);

				}
			}
			else {
				injury=foundInjury;
				injury.setRecovered(true);
				injuryRepository.save(injury);
				FactHandle factHandle = kieSessionProvider.getKieSession().getFactHandle(injury);

				if (factHandle != null) {
					kieSessionProvider.getKieSession().update(factHandle, injury);
				} else {
					kieSessionProvider.getKieSession().insert(injury);
				}
			}

		}
		kieSessionProvider.getKieSession().fireAllRules();
		return ResponseEntity.noContent().build();


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


	@GetMapping (value = "/recommend")
	public ResponseEntity<?> recommendationList(@RequestHeader("Authorization") String authHeader) {

		String token = authHeader.substring(7);
		int userId = tokenUtils.getIdFromToken(token);

		Optional<Manager> manager = managerRepository.findById(userId);
		if (manager.isPresent()) {

			FantasyTeam fantasyTeam=manager.get().getTeam();

			RecommendationList rl = new RecommendationList();
			PreferencesList pl = new PreferencesList(new HashSet<>(), fantasyTeam);
			KieServices ks = KieServices.Factory.get();


			Collection<?> recommendationLists = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
				@Override
				public boolean accept(Object object) {
					return object instanceof RecommendationList;
				}
			});

			if (recommendationLists.isEmpty()) {
				kieSessionProvider.getKieSession().insert(rl);

				kieSessionProvider.getKieSession().fireAllRules();
			} else {
				rl = (RecommendationList) recommendationLists.iterator().next();

			}

			Collection<?> preferencesLists = kieSessionProvider.getKieSession().getObjects(new ObjectFilter() {
				@Override
				public boolean accept(Object object) {
					return object instanceof PreferencesList;
				}
			});

			if (preferencesLists.isEmpty()) {
				kieSessionProvider.getKieSession().insert(pl);

				kieSessionProvider.getKieSession().fireAllRules();
			} else {
//				PreferencesList pl1 = (PreferencesList) preferencesLists.iterator().next();
				for(Object pl1: preferencesLists)

					if(Objects.equals(((PreferencesList) pl1).getTeam().getId(), pl.getTeam().getId()))
						pl=(PreferencesList) pl1;
			}


			return ResponseEntity.ok(new RecommendationListDTO(rl, pl, fantasyTeam));
		}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");

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
