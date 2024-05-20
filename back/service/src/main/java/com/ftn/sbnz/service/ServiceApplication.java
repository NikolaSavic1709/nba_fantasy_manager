package com.ftn.sbnz.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.repository.INBATeamRepository;
import com.ftn.sbnz.repository.players.IInjuryRepository;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import com.ftn.sbnz.repository.players.IStatisticalColumnsRepository;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootApplication
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
	private IStatisticalColumnsRepository statisticalColumnsRepository;
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
	@Bean
	public KieSession kieSession(){
		KieContainer kieContainer= this.kieContainer();
		KieSession kieSession = kieContainer.newKieSession("fwKsession");
		return kieSession;
	}
	
	/*
	 * KieServices ks = KieServices.Factory.get(); KieContainer kContainer =
	 * ks.newKieContainer(ks.newReleaseId("drools-spring-v2",
	 * "drools-spring-v2-kjar", "0.0.1-SNAPSHOT")); KieScanner kScanner =
	 * ks.newKieScanner(kContainer); kScanner.start(10_000); KieSession kSession =
	 * kContainer.newKieSession();
	 */

	private void readData(){
		KieSession kieSession= this.kieSession();

		String teamsCsvFile="../data/teams.csv";
		String injuriesCsvFile = "../data/Injury_History.csv";
		SimpleDateFormat injuriesDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String playersCsvFile="../data/nba2k-full.csv";
		SimpleDateFormat playersDateFormat = new SimpleDateFormat("MM/dd/yyyy");

		List<Player> players=new ArrayList<>();
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

		try (Reader reader = new FileReader(playersCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			for (CSVRecord csvRecord : csvParser) {
				// Extract fields from each record
				String name = csvRecord.get("full_name");
				String price = csvRecord.get("rating");
				String teamName= csvRecord.get("team");

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
					player.setPrice(Integer.parseInt(price));

					Optional<NBATeam> result = teams.stream()
							.filter(team -> teamName.equals(team.getName()))
							.findFirst();

                    result.ifPresent(player::setNbaTeam);
                    result.ifPresent(nbaTeam -> nbaTeam.getPlayers().add(player));
					players.add(player);
				}
			}

		}
		catch (IOException | ParseException e) {
			e.printStackTrace();
		}


//		NBATeam phoenix=new NBATeam();
//		phoenix.setName("Phoenix");
//		phoenix.setPlayers(new ArrayList<>());
		try (Reader reader = new FileReader(injuriesCsvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

//			String lastReadName="";
			Player lastReadPlayer=new Player();
			lastReadPlayer.setName("");
			List<Injury> injuries=new ArrayList<>();
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
					Injury injury=new Injury((long) injuries.size(),"",notes,false,null, null, injuryTimestamp, current_player);
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
			for (NBATeam team : teams){
				kieSession.insert(team);
				nbaTeamRepository.save(team);
			}

			for (Player player : players) {
				kieSession.insert(player);
				StatisticalColumns gordonoveKolone = new StatisticalColumns();
				gordonoveKolone.setGp(0);
				statisticalColumnsRepository.save(gordonoveKolone);
				player.setStatisticalColumns(gordonoveKolone);
				player.setTotalBonusPoints(0);
				playerRepository.save(player);
			}
			for (Injury injury : injuries){
				kieSession.insert(injury);
				injuryRepository.save(injury);
			}
			kieSession.fireAllRules();
//			kieSession.dispose();

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
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

}
