package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.events.PlayerAdditionEvent;
import com.ftn.sbnz.model.events.unused.FlightArrivalEvent;
import com.ftn.sbnz.model.events.unused.TransactionEvent;
import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.NBATeam;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PreferencesList;
import com.ftn.sbnz.model.models.unused.Customer;
import com.ftn.sbnz.model.models.unused.Man;
import com.ftn.sbnz.model.models.unused.Parent;
import com.ftn.sbnz.model.models.unused.Woman;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.junit.Ignore;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Disabled;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionPseudoClock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
// import org.kie.api.KieServices;
// import org.kie.api.runtime.KieContainer;
// import org.kie.api.runtime.KieSession;



public class CEPConfigTest {

    @Test
    @Ignore
    public void proba(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");
        SessionPseudoClock clock=ksession.getSessionClock();
        System.out.println(clock.getCurrentTime());
        Player player=new Player();
        FantasyTeam team=new FantasyTeam();
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player,team));
        int ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(0));

        clock.advanceTime(8, TimeUnit.DAYS);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player,team));
        ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(0));

        clock.advanceTime(6, TimeUnit.DAYS);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player,team));
        ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(1));

        ksession.dispose();

    }
    @Test
    public void testThreePlayersFromSameTeam() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");
        SessionPseudoClock clock=ksession.getSessionClock();

        NBATeam team1=new NBATeam();
        team1.setId(1L);
        NBATeam team2=new NBATeam();
        team2.setId(2L);

        FantasyTeam fTeam1=new FantasyTeam();
        fTeam1.setId(1L);
        FantasyTeam fTeam2=new FantasyTeam();
        fTeam2.setId(2L);

        Player player1=new Player();
        player1.setName("prvi80");
        Player player2=new Player();
        player2.setName("drugi84");
        Player player3=new Player();
        player3.setName("treci83");
        Player player4=new Player();
        player4.setName("cetvrti82");
        player1.setNbaTeam(team1);
        player2.setNbaTeam(team1);

        player3.setNbaTeam(team1);
        player4.setNbaTeam(team1);

        player1.setFantasyTeam(fTeam1);
        player2.setFantasyTeam(fTeam1);

        player3.setFantasyTeam(fTeam1);
        player4.setFantasyTeam(fTeam2);
        player1.setPrice(80);
        player2.setPrice(84);
        player3.setPrice(83);
        player4.setPrice(82);
        player1.setPosition(new ArrayList<>(List.of(1)));
        player2.setPosition(new ArrayList<>(List.of(2)));
        player3.setPosition(new ArrayList<>(List.of(3)));
        player4.setPosition(new ArrayList<>(List.of(4)));

        PreferencesList pl=new PreferencesList();
        pl.setTeam(fTeam1);
        pl.setPlayers(new HashSet<>());

        ksession.insert(pl);

        ksession.insert(player1);
        ksession.insert(player2);
        ksession.insert(player3);
        ksession.insert(player4);
        ksession.insert(team1);
        ksession.insert(team2);

        ksession.insert(fTeam1);
        ksession.insert(fTeam2);
        Collection<Object> obj= (Collection<Object>) ksession.getObjects();
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player2,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player3,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player4,fTeam2));

        int ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(1));

        ksession.dispose();


    }

    @Test
    public void testThreePlayersInTenMinutesAndPenalties(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");
        SessionPseudoClock clock=ksession.getSessionClock();

        NBATeam team1=new NBATeam();
        team1.setId(1L);
        NBATeam team2=new NBATeam();
        team2.setId(2L);
        NBATeam team3=new NBATeam();
        team3.setId(3L);
        NBATeam team4=new NBATeam();
        team4.setId(4L);


        FantasyTeam fTeam1=new FantasyTeam();
        fTeam1.setId(1L);
        FantasyTeam fTeam2=new FantasyTeam();
        fTeam2.setId(2L);
        fTeam1.setTotalPoints(100);
        fTeam2.setTotalPoints(0);

        Player player1=new Player();
        player1.setName("prvi80");
        Player player2=new Player();
        player2.setName("drugi84");
        Player player3=new Player();
        player3.setName("treci83");
        Player player4=new Player();
        player4.setName("cetvrti82");
        player1.setNbaTeam(team1);
        player2.setNbaTeam(team2);

        player3.setNbaTeam(team3);
        player4.setNbaTeam(team4);

        player1.setFantasyTeam(fTeam1);
        player2.setFantasyTeam(fTeam1);

        player3.setFantasyTeam(fTeam1);
        player4.setFantasyTeam(fTeam1);
        player1.setPrice(80);
        player2.setPrice(84);
        player3.setPrice(83);
        player4.setPrice(82);
        player1.setPosition(new ArrayList<>(List.of(1)));
        player2.setPosition(new ArrayList<>(List.of(2)));
        player3.setPosition(new ArrayList<>(List.of(3)));
        player4.setPosition(new ArrayList<>(List.of(4)));

        PreferencesList pl=new PreferencesList();
        pl.setTeam(fTeam1);
        pl.setPlayers(new HashSet<>());

        ksession.insert(pl);

        ksession.insert(player1);
        ksession.insert(player2);
        ksession.insert(player3);
        ksession.insert(player4);
        ksession.insert(team1);
        ksession.insert(team2);
        ksession.insert(team3);
        ksession.insert(team4);

        ksession.insert(fTeam1);
        ksession.insert(fTeam2);

        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(1, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player2,fTeam1));
        clock.advanceTime(1, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player3,fTeam1));

        int ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(1));

        clock.advanceTime(1, TimeUnit.DAYS);

        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(1, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player2,fTeam1));
        clock.advanceTime(1, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player4,fTeam1));

        ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(2));

        clock.advanceTime(1, TimeUnit.DAYS);

        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(1, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player4,fTeam1));
        clock.advanceTime(1, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player3,fTeam1));

        ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(4));

        ksession.dispose();
    }

    @Test
    public void testPlayersPriceRange() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");
        SessionPseudoClock clock=ksession.getSessionClock();

        NBATeam team1=new NBATeam();
        team1.setId(1L);
        NBATeam team2=new NBATeam();
        team2.setId(2L);
        NBATeam team3=new NBATeam();
        team3.setId(3L);
        NBATeam team4=new NBATeam();
        team4.setId(4L);

        FantasyTeam fTeam1=new FantasyTeam();
        fTeam1.setId(1L);
        FantasyTeam fTeam2=new FantasyTeam();
        fTeam2.setId(2L);

        Player player1=new Player();
        player1.setName("prvi80");
        Player player2=new Player();
        player2.setName("drugi84");
        Player player3=new Player();
        player3.setName("treci83");
        Player player4=new Player();
        player4.setName("cetvrti82");
        Player player5=new Player();
        player5.setName("peti87");
        Player player6=new Player();
        player6.setName("sesti77");
        player1.setNbaTeam(team1);
        player2.setNbaTeam(team1);

        player3.setNbaTeam(team2);
        player4.setNbaTeam(team2);

        player5.setNbaTeam(team3);
        player6.setNbaTeam(team3);

        player1.setFantasyTeam(fTeam1);
        player2.setFantasyTeam(fTeam1);

        player3.setFantasyTeam(fTeam1);
        player4.setFantasyTeam(fTeam1);

        player5.setFantasyTeam(fTeam1);
//        player6.setFantasyTeam(fTeam1);

        player1.setPrice(80);
        player2.setPrice(84);
        player3.setPrice(83);
        player4.setPrice(82);
        player5.setPrice(87);
        player6.setPrice(86);
        player1.setPosition(new ArrayList<>(List.of(1)));
        player2.setPosition(new ArrayList<>(List.of(2)));
        player3.setPosition(new ArrayList<>(List.of(3)));
        player4.setPosition(new ArrayList<>(List.of(4)));
        player5.setPosition(new ArrayList<>(List.of(5)));
        player6.setPosition(new ArrayList<>(List.of(4)));
        PreferencesList pl=new PreferencesList();
        pl.setTeam(fTeam1);
        pl.setPlayers(new HashSet<>());

        ksession.insert(pl);

        ksession.insert(player1);
        ksession.insert(player2);
        ksession.insert(player3);
        ksession.insert(player4);
        ksession.insert(player5);
        ksession.insert(player6);
        ksession.insert(team1);
        ksession.insert(team2);
        ksession.insert(team3);
        ksession.insert(team4);

        ksession.insert(fTeam1);
        ksession.insert(fTeam2);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(1, TimeUnit.DAYS);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player2,fTeam1));
        clock.advanceTime(1, TimeUnit.DAYS);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player3,fTeam1));
        clock.advanceTime(1, TimeUnit.DAYS);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player4,fTeam1));
        clock.advanceTime(1, TimeUnit.DAYS);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player5,fTeam1));
        int ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(1));

        ksession.dispose();


    }

    @Test
    public void testFivePlayersWithSamePositions() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");
        SessionPseudoClock clock=ksession.getSessionClock();

        NBATeam team1=new NBATeam();
        team1.setId(1L);
        NBATeam team2=new NBATeam();
        team2.setId(2L);
        NBATeam team3=new NBATeam();
        team3.setId(3L);
        NBATeam team4=new NBATeam();
        team4.setId(4L);

        FantasyTeam fTeam1=new FantasyTeam();
        fTeam1.setId(1L);
        FantasyTeam fTeam2=new FantasyTeam();
        fTeam2.setId(2L);

        Player player1=new Player();
        player1.setName("prvi80");
        Player player2=new Player();
        player2.setName("drugi84");
        Player player3=new Player();
        player3.setName("treci83");
        Player player4=new Player();
        player4.setName("cetvrti82");
        Player player5=new Player();
        player5.setName("peti87");
        Player player6=new Player();
        player6.setName("sesti77");
        player1.setNbaTeam(team1);
        player2.setNbaTeam(team1);

        player3.setNbaTeam(team2);
        player4.setNbaTeam(team2);

        player5.setNbaTeam(team3);
        player6.setNbaTeam(team3);

        player1.setFantasyTeam(fTeam1);
        player2.setFantasyTeam(fTeam1);

        player3.setFantasyTeam(fTeam1);
        player4.setFantasyTeam(fTeam2);

        player5.setFantasyTeam(fTeam1);
        player6.setFantasyTeam(fTeam1);

        player1.setPrice(80);
        player2.setPrice(84);
        player3.setPrice(83);
        player4.setPrice(82);
        player5.setPrice(87);
        player6.setPrice(86);
        player1.setPosition(new ArrayList<>(List.of(5)));
        player2.setPosition(new ArrayList<>(List.of(4,5)));
        player3.setPosition(new ArrayList<>(List.of(5)));
        player4.setPosition(new ArrayList<>(List.of(5)));
        player5.setPosition(new ArrayList<>(List.of(5)));
        player6.setPosition(new ArrayList<>(List.of(5,4)));
        PreferencesList pl=new PreferencesList();
        pl.setTeam(fTeam1);
        pl.setPlayers(new HashSet<>());

        ksession.insert(pl);

        ksession.insert(player1);
        ksession.insert(player2);
        ksession.insert(player3);
        ksession.insert(player4);
        ksession.insert(player5);
        ksession.insert(player6);
        ksession.insert(team1);
        ksession.insert(team2);
        ksession.insert(team3);
        ksession.insert(team4);

        ksession.insert(fTeam1);
        ksession.insert(fTeam2);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player2,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player3,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player4,fTeam2));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player5,fTeam1));
        int ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(0));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player6,fTeam1));

        ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(1));

        ksession.dispose();


    }

}
