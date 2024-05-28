package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.events.PlayerAdditionEvent;
import com.ftn.sbnz.model.events.unused.FlightArrivalEvent;
import com.ftn.sbnz.model.events.unused.TransactionEvent;
import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.NBATeam;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.unused.Customer;
import com.ftn.sbnz.model.models.unused.Man;
import com.ftn.sbnz.model.models.unused.Parent;
import com.ftn.sbnz.model.models.unused.Woman;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.time.SessionPseudoClock;

import java.util.concurrent.TimeUnit;
// import org.kie.api.KieServices;
// import org.kie.api.runtime.KieContainer;
// import org.kie.api.runtime.KieSession;



public class CEPConfigTest {

    @Test
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
        player1.setName("1");
        Player player2=new Player();
        player2.setName("2");
        Player player3=new Player();
        player3.setName("3");
        Player player4=new Player();
        player4.setName("4");
        player1.setNbaTeam(team1);
        player2.setNbaTeam(team1);

        player3.setNbaTeam(team1);
        player4.setNbaTeam(team2);
        ksession.insert(player1);
        ksession.insert(player2);
        ksession.insert(player3);
        ksession.insert(player4);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player1,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player3,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player2,fTeam1));
        clock.advanceTime(8, TimeUnit.MINUTES);
        ksession.insert(new PlayerAdditionEvent(clock.getCurrentTime(),player4,fTeam2));

        int ruleCount= ksession.fireAllRules();
        assertThat(ruleCount,equalTo(1));

        ksession.dispose();


    }
}
