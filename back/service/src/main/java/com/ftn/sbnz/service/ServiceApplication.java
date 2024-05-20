package com.ftn.sbnz.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.ftn.sbnz.model.models.Injury;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PlayerStatus;
import org.kie.api.runtime.KieSession;
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
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ServiceApplication.class, args);

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);

		StringBuilder sb = new StringBuilder("Application beans:\n");
		for (String beanName : beanNames) {
			sb.append(beanName + "\n");
		}
		ServiceApplication app = ctx.getBean(ServiceApplication.class);
		app.readInjuryData();
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

	private void readInjuryData(){
//		KieContainer kieContainer= this.kieContainer();
//		KieSession kieSession = kieContainer.newKieSession("fwKsession");
		KieSession kieSession= this.kieSession();

		System.out.println("readdata");
		System.out.println(kieSession);

		String csvFile = "../data/injuries.csv"; // Path to your CSV file
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


		try (Reader reader = new FileReader(csvFile);
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

			List<CsvRecord> records = new ArrayList<>();
//			String lastReadName="";
			Player lastReadPlayer=new Player();
			lastReadPlayer.setName("");
			List<Injury> injuries=new ArrayList<>();
			List<Player> players=new ArrayList<>();
			for (CSVRecord csvRecord : csvParser) {
				// Extract fields from each record
				String name = csvRecord.get("Name");
				String team = csvRecord.get("Team");
				String position = csvRecord.get("Position");
				String date = csvRecord.get("Date");
				String notes = csvRecord.get("Notes");

				if(!Objects.equals(name, lastReadPlayer.getName())) {
					lastReadPlayer.setName(name);
					Player player=new Player();
					player.setName(name);
					player.setStatus(PlayerStatus.HEALTHY);
					players.add(player);

				}
				if(!isRecovery(notes)){
					Date injuryTimestamp = dateFormat.parse(date);
					Injury injury=new Injury((long) injuries.size(),"",notes,false,null, null, injuryTimestamp, players.get(players.size()-1));
					if(injuries.size()==0 || injuries.get(injuries.size()-1).isRecovered())
						injuries.add(injury);
					else
						injuries.set(injuries.size() - 1, injury);
				}
				else{
					Date recoveryTimestamp = dateFormat.parse(date);
					Injury injury= injuries.get(injuries.size()-1);
					injury.setRecovered(true);
					long differenceInMilliseconds = recoveryTimestamp.getTime() - injury.getTimestamp().getTime();

					// Convert the difference from milliseconds to days
					Integer differenceInDays = Math.toIntExact(TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS));
					if(differenceInDays==0)
						injury.setRecovered(false);
					injury.setRecoveryTimeInDays(differenceInDays);

				}

				// Create a CsvRecord object to hold the extracted fields
//				CsvRecord record = new CsvRecord(name, team, position, date, notes);
//				records.add(record);
			}

			// Process the extracted records as needed
//			for (CsvRecord record : records) {
//				System.out.println(record);
//			}
			for (Player player : players) {
				kieSession.insert(player);
			}
			for (Injury injury : injuries){
				kieSession.insert(injury);
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
	static class CsvRecord {
		String name;
		String team;
		String position;
		String date;
		String notes;

		public CsvRecord(String name, String team, String position, String date, String notes) {
			this.name = name;
			this.team = team;
			this.position = position;
			this.date = date;
			this.notes = notes;
		}

		@Override
		public String toString() {
			return "CsvRecord{" +
					"name='" + name + '\'' +
					", team='" + team + '\'' +
					", position='" + position + '\'' +
					", date='" + date + '\'' +
					", notes='" + notes + '\'' +
					'}';
		}
	}
}
