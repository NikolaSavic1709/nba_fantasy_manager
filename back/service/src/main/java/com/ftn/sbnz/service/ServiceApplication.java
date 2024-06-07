package com.ftn.sbnz.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.model.models.stats.FantasyStatisticalColumns;
import com.ftn.sbnz.repository.IFantasyTeamRepository;
import com.ftn.sbnz.repository.IFilterRepository;
import com.ftn.sbnz.model.models.injuries.Injury;
import com.ftn.sbnz.model.models.injuries.InjuryHistoryData;
import com.ftn.sbnz.model.models.stats.StatisticalColumns;
import com.ftn.sbnz.repository.INBATeamRepository;
import com.ftn.sbnz.repository.players.IFantasyStatisticalColumnsRepository;
import com.ftn.sbnz.repository.players.IInjuryRepository;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import com.ftn.sbnz.repository.players.IStatisticalColumnsRepository;
import com.ftn.sbnz.utils.KieSessionProvider;
import com.ftn.sbnz.utils.TemplateLoader;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.kie.api.conf.KieBaseOption;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ftn.sbnz"})
@EnableJpaRepositories(basePackages = "com.ftn.sbnz.repository")
@EntityScan(basePackages = "com.ftn.sbnz.model.models")
public class ServiceApplication  {
	
	private static Logger log = LoggerFactory.getLogger(ServiceApplication.class);
	@Autowired
	private IPlayerRepository playerRepository;
	@Autowired
	private IInjuryRepository injuryRepository;
	@Autowired
	private INBATeamRepository nbaTeamRepository;
	@Autowired
	private IFantasyTeamRepository fantasyTeamRepository;
	@Autowired
	private IFilterRepository filterRepository;
	@Autowired
	private TemplateLoader templateLoader;

	@Autowired
	private KieSessionProvider kieSessionProvider;

	@Autowired
	private IStatisticalColumnsRepository statisticalColumnsRepository;

	@Autowired
	private IFantasyStatisticalColumnsRepository fantasyStatisticalColumnsRepository;

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ServiceApplication.class, args);

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);

		StringBuilder sb = new StringBuilder("Application beans:\n");
		for (String beanName : beanNames) {
			sb.append(beanName + "\n");
		}
		ServiceApplication app = ctx.getBean(ServiceApplication.class);
		app.readData();
//		log.info(sb.toString());
	}

	@Bean
	public KieContainer kieContainer() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("com.ftn.sbnz", "kjar", "0.0.1-SNAPSHOT"));
//		KieContainer kContainer = ks.getKieClasspathContainer();
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(1000);
		return kContainer;
	}


//	@Bean()
//	public KieSession kieSession(){
////		KieContainer kieContainer= this.kieContainer();
////		KieSession kieSession = kieContainer.newKieSession("fwKsession");
////		return kieSession;
//
//		//KieHelper kieHelper = TemplateLoader.loadFromObjects();
//		KieHelper kieHelper = templateLoader.loadFromObjects();
//		KieSession kieSession = kieHelper.build().newKieSession();
//		TemplateLoader.getNumberOfRules(kieSession);
//		return kieSession;

		//KieHelper kieHelper = TemplateLoader.loadFromObjects();


//		KieHelper kieHelper = TemplateLoader.loadFromSpreadsheet();
//		KieSession kieSession = kieHelper.build().newKieSession();
//		TemplateLoader.getNumberOfRules(kieSession);
//		return kieSession;

	// 	KieHelper kieHelper = TemplateLoader.loadFromSpreadsheet();

	// 	KieServices kieServices = KieServices.Factory.get();

	// 	KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
	// 	kieBaseConfiguration.setOption(EventProcessingOption.STREAM);

	// 	KieBase kieBase = kieHelper.build(kieBaseConfiguration);

	// 	KieSession kieSession = kieBase.newKieSession();

	// 	TemplateLoader.getNumberOfRules(kieSession);

	// 	return kieSession;
	// }

	
	/*
	 * KieServices ks = KieServices.Factory.get(); KieContainer kContainer =
	 * ks.newKieContainer(ks.newReleaseId("drools-spring-v2",
	 * "drools-spring-v2-kjar", "0.0.1-SNAPSHOT")); KieScanner kScanner =
	 * ks.newKieScanner(kContainer); kScanner.start(10_000); KieSession kSession =
	 * kContainer.newKieSession();
	 */

	private void readData(){
		KieSession kieSession= this.kieSessionProvider.getKieSession();

		String injuriesBackwardCsvFile="../data/injuries-backward.csv";
		String teamsCsvFile="../data/teams.csv";
		String injuriesCsvFile = "../data/injuries.csv";
		SimpleDateFormat injuriesDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String playersCsvFile="../data/nba2k-full.csv";
		SimpleDateFormat playersDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String playersStatisticCsvFile="../data/players_percentage.csv";

		List<String> injuryLevels=new ArrayList<>();
		List<String> specificBodyParts=new ArrayList<>();
		List<String> bodyParts=new ArrayList<>();

		try (Reader reader = new FileReader(injuriesBackwardCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			for (CSVRecord csvRecord : csvParser) {
				String injurySeverityLevel=csvRecord.get("Injury_Severity_Level");
				if (!Objects.equals(injurySeverityLevel, ""))
					injuryLevels.add(injurySeverityLevel);
				String specificBodyPart = csvRecord.get("Specific_Body_Part");
				if (!Objects.equals(specificBodyPart, "")) {
					specificBodyParts.add(specificBodyPart);
				}
				String bodyPart = csvRecord.get("Body_Part");
				if (!Objects.equals(bodyPart, "")) {
					bodyParts.add(bodyPart);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		List<NBATeam> teams=readTeams(teamsCsvFile);
		List<Player> players=readPlayers(playersCsvFile, playersStatisticCsvFile, teams, playersDateFormat);

		List<Injury> injuries=readInjuries(injuriesCsvFile, players, injuriesDateFormat);
		List<InjuryHistoryData> injuryHistoryData=createInjuryTree(injuries, injuryLevels, specificBodyParts, bodyParts);

		for (NBATeam team : teams){
			kieSession.insert(team);
			nbaTeamRepository.save(team);
		}
		for (Player player : players) {
			kieSession.insert(player.getStatisticalColumns());
			statisticalColumnsRepository.save(player.getStatisticalColumns());
			fantasyStatisticalColumnsRepository.save(player.getFantasyStatisticalColumns());
			playerRepository.save(player);
		}
		for (Player player : players) {
			kieSession.insert(player);
//			playerRepository.save(player);
		}
		for (Injury injury : injuries){
			kieSession.insert(injury);
			injuryRepository.save(injury);
		}
		FantasyTeam fantasyTeam=fantasyTeamRepository.findById(1).get();
		for(int i=1;i<10;i++)
		{
			Player player=players.get(i);
			player.setFantasyTeam(fantasyTeam);
			playerRepository.save(player);
		}

		for (InjuryHistoryData injury: injuryHistoryData){
			kieSession.insert(injury);
		}
		kieSession.fireAllRules();
		System.out.println("gotovo");
    }
	private List<NBATeam> readTeams(String teamsCsvFile){
		List<NBATeam> teams=new ArrayList<>();
		try (Reader reader = new FileReader(teamsCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			for (CSVRecord csvRecord : csvParser) {
				String name = csvRecord.get("team_name");
				NBATeam team=new NBATeam();
				team.setName(name);
				team.setPlayers(new ArrayList<>());
				teams.add(team);

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return teams;
	}
	private List<Player> readPlayers(String playersCsvFile, String playersStatisticCsvFile, List<NBATeam> teams, SimpleDateFormat playersDateFormat){
		List<Player> players=new ArrayList<>();
		Map<String, List<Integer>> positionMap = new HashMap<>();

		positionMap.put("PG", Arrays.asList(1, 2));
		positionMap.put("SG", Arrays.asList(2, 3, 1));
		positionMap.put("SF", Arrays.asList(3, 4, 2));
		positionMap.put("PF", Arrays.asList(4, 5, 3));
		positionMap.put("C", Arrays.asList(5, 4));
		positionMap.put("SF-PF", Arrays.asList(3, 4));
		positionMap.put("PF-SF", Arrays.asList(4, 3));
		positionMap.put("SF-SG", Arrays.asList(3, 2));
		positionMap.put("PF-C", Arrays.asList(4, 5));
		try (Reader reader = new FileReader(playersCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			for (CSVRecord csvRecord : csvParser) {
				String name = csvRecord.get("full_name");
				String price = csvRecord.get("rating");
				String teamName= csvRecord.get("team");
				String style = csvRecord.get("style");

				// pozicija
				String birthday = csvRecord.get("b_day");
				String nationality= csvRecord.get("country");
				String nba2kVersion=csvRecord.get("version");
				if(Objects.equals(nba2kVersion, "NBA2k20") && !Objects.equals(teamName, ""))
				{
					Player player=new Player();
					player.setName(name);
					player.setBirthDate(playersDateFormat.parse(birthday));
					player.setNationality(nationality);
					player.setStatus(PlayerStatus.HEALTHY);
					player.setTotalBonusPoints(0);
					player.setTotalFantasyPoints(0);
					player.setPrice(Integer.parseInt(price));

					Optional<NBATeam> result = teams.stream()
							.filter(team -> teamName.equals(team.getName()))
							.findFirst();

					if(!Objects.equals(style, "")){
						player.setPlayerStyle(getPlayerByName(players, style));
					}

                    result.ifPresent(player::setNbaTeam);

					player.setFantasyStatisticalColumns(new FantasyStatisticalColumns());
					players.add(player);
				}
			}

		}
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		try (Reader reader = new FileReader(playersStatisticCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			for (CSVRecord csvRecord : csvParser) {
				String name = csvRecord.get("Player");
				String position= csvRecord.get("Pos");

				StatisticalColumns statisticalColumns=new StatisticalColumns();
				statisticalColumns.setGp(Integer.parseInt(csvRecord.get("G")));
				statisticalColumns.setPpg(Double.parseDouble(csvRecord.get("PTS")));
				statisticalColumns.setApg(Double.parseDouble(csvRecord.get("AST")));
				statisticalColumns.setRpg(Double.parseDouble(csvRecord.get("TRB")));
				statisticalColumns.setTpg(Double.parseDouble(csvRecord.get("TOV")));
				statisticalColumns.setSpg(Double.parseDouble(csvRecord.get("STL")));
				statisticalColumns.setBpg(Double.parseDouble(csvRecord.get("BLK")));
				statisticalColumns.setPfpg(Double.parseDouble(csvRecord.get("PF")));
				statisticalColumns.setMpg(Double.parseDouble(csvRecord.get("MP")));
				statisticalColumns.setFgPercentage(Double.parseDouble(csvRecord.get("FG%")));
				statisticalColumns.setTwoPointPercentage(Double.parseDouble(csvRecord.get("2P%")));
				statisticalColumns.setThreePointPercentage(Double.parseDouble(csvRecord.get("3P%")));


				Optional<Player> result = players.stream()
						.filter(player -> name.equals(player.getName()))
						.findFirst();

//				if(result.isEmpty())
//					System.out.println(name);

                result.ifPresent(player -> player.setStatisticalColumns(statisticalColumns));
				result.ifPresent(player -> player.setPosition(positionMap.get(position)));
//				result.ifPresent(statisticalColumns::setPlayer);
//				result.ifPresent(player -> player.getNbaTeam().getPlayers().add(player));
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}



		return players.stream()
				.filter(player -> player.getStatisticalColumns() != null)
				.collect(Collectors.toList());
//				.filter(player -> player.getStatisticalColumns() != null)
//				.filter(player -> player.getPlayerStyle() != null)
//				.filter(player -> player.getPlayerStyle().getStatisticalColumns() == null)
//				.peek(player -> player.setPlayerStyle(null))
//				.collect(Collectors.toList());
	}
	private List<Injury> readInjuries(String injuriesCsvFile, List<Player> players, SimpleDateFormat injuriesDateFormat){
		List<Injury> injuries=new ArrayList<>();
		try (Reader reader = new FileReader(injuriesCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

//			String lastReadName="";
			Player lastReadPlayer=new Player();
			lastReadPlayer.setName("");
			Player current_player=players.get(0);
			for (CSVRecord csvRecord : csvParser) {
				// Extract fields from each record
				String name = csvRecord.get("Name");
				String team = csvRecord.get("Team");
				String position = csvRecord.get("Position");
				String date = csvRecord.get("Date");
				String notes = csvRecord.get("Notes");

				if(!Objects.equals(name, lastReadPlayer.getName())) {
					lastReadPlayer.setName(name);
					Optional<Player> result = players.stream()
							.filter(player -> name.equals(player.getName()))
							.findFirst();

					if(result.isPresent())
						current_player=result.get();
					else
						continue;

				}
				if(!isRecovery(notes)){
					Date injuryTimestamp = injuriesDateFormat.parse(date);
					Injury injury=new Injury((long) injuries.size(),new ArrayList<>(),notes,false,null, null, injuryTimestamp, current_player);
					if(injuries.size()==0 || injuries.get(injuries.size()-1).isRecovered())
						injuries.add(injury);
					else
						injuries.set(injuries.size() - 1, injury);
				}
				else{
					Date recoveryTimestamp = injuriesDateFormat.parse(date);
					Injury injury= injuries.get(injuries.size()-1);
					injury.setRecovered(true);
					long differenceInMilliseconds = recoveryTimestamp.getTime() - injury.getTimestamp().getTime();

					// Convert the difference from milliseconds to days
					Integer differenceInDays = Math.toIntExact(TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS));
					if(differenceInDays==0)
						injury.setRecovered(false);
					injury.setRecoveryTimeInDays(differenceInDays);

				}

			}


		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return injuries;
	}

	private List<InjuryHistoryData> createInjuryTree(List<Injury> injuries, List<String> injuryLevels, List<String> specificBodyParts, List<String> bodyParts){


		Map<String, InjuryHistoryData> injuryHistoryDataMap = new HashMap<>();

		InjuryHistoryData root=new InjuryHistoryData(0,"",0.0,0);
		injuryHistoryDataMap.put("",root);
		String foundInjuryLevel, foundSpecificBodyPart, foundBodyPart;

		int currId=1;

		for (Injury injury: injuries)
		{
			foundInjuryLevel=null;
			foundSpecificBodyPart=null;
			foundBodyPart=null;

			for(String bodyPart: bodyParts)
				if(injury.getDescription().toLowerCase().contains(bodyPart.toLowerCase()))
					foundBodyPart=bodyPart.toLowerCase();
			if (foundBodyPart!=null)
			{
				for(String injuryLevel: injuryLevels)
					if(injury.getDescription().toLowerCase().contains(injuryLevel.toLowerCase()))
						foundInjuryLevel=injuryLevel.toLowerCase();
				if (foundInjuryLevel!=null) {
					for (String specificBodyPart : specificBodyParts)
						if (injury.getDescription().toLowerCase().contains(specificBodyPart.toLowerCase()))
							foundSpecificBodyPart = specificBodyPart.toLowerCase();
					if (foundSpecificBodyPart!=null)
					{
						InjuryHistoryData ihd=injuryHistoryDataMap.get(foundBodyPart+"_"+foundInjuryLevel+"_"+foundSpecificBodyPart);
						if(ihd==null)
							injuryHistoryDataMap.put(foundBodyPart+"_"+foundInjuryLevel+"_"+foundSpecificBodyPart, new InjuryHistoryData(currId++, foundBodyPart+"_"+foundInjuryLevel+"_"+foundSpecificBodyPart, injury.getRecoveryTimeInDays(), 1));
						else {
							ihd.setInjuryCount(ihd.getInjuryCount()+1);
							ihd.setTotalDays(ihd.getTotalDays()+ injury.getRecoveryTimeInDays());
						}
					}
					InjuryHistoryData ihd=injuryHistoryDataMap.get(foundBodyPart+"_"+foundInjuryLevel);
					if(ihd==null)
						injuryHistoryDataMap.put(foundBodyPart+"_"+foundInjuryLevel, new InjuryHistoryData(currId++,foundBodyPart+"_"+foundInjuryLevel, injury.getRecoveryTimeInDays(), 1));
					else {
						ihd.setInjuryCount(ihd.getInjuryCount()+1);
						ihd.setTotalDays(ihd.getTotalDays()+ injury.getRecoveryTimeInDays());
					}
				}
				InjuryHistoryData ihd=injuryHistoryDataMap.get(foundBodyPart);
				if(ihd==null)
					injuryHistoryDataMap.put(foundBodyPart, new InjuryHistoryData(currId++, foundBodyPart, injury.getRecoveryTimeInDays(), 1));
				else {
					ihd.setInjuryCount(ihd.getInjuryCount()+1);
					ihd.setTotalDays(ihd.getTotalDays()+ injury.getRecoveryTimeInDays());
				}
			}
			root.setInjuryCount(root.getInjuryCount()+1);
			root.setTotalDays(root.getTotalDays()+injury.getRecoveryTimeInDays());

		}
		return  new ArrayList<>(injuryHistoryDataMap.values());
	}
	private boolean isRecovery(String target){
		List<String> recoveryStrings=new ArrayList<>();
		recoveryStrings.add("returned to lineup");
		recoveryStrings.add("activated from IL");
		boolean found = false;
		for (String str : recoveryStrings) {
			if (str.equals(target)) {
				found = true;
				break;
			}
		}
		return found;
	}

	private Player getPlayerByName(List<Player> players, String name) {
		Optional<Player> playerOptional = players.stream()
				.filter(player -> player.getName().equals(name))
				.findFirst();
		return playerOptional.orElse(null); // or throw an exception if the player is not found
	}

}
