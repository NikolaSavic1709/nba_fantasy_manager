package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.events.FlightArrivalEvent;
import com.ftn.sbnz.model.events.TransactionEvent;
import com.ftn.sbnz.model.models.*;
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
    public void testBackward(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("bwKsession");
        // grand parents
        ksession.insert(new Man("john"));
        ksession.insert(new Woman("janet"));
// parent
        ksession.insert(new Man("adam"));
        ksession.insert(new Parent("john", "adam"));
        ksession.insert(new Parent("janet", "adam"));
        ksession.insert(new Man("stan"));
        ksession.insert(new Parent("john", "stan"));
        ksession.insert(new Parent("janet", "stan"));
// grand parents
        ksession.insert(new Man("carl"));
        ksession.insert(new Woman("tina"));
//
// parent
        ksession.insert(new Woman("eve"));
        ksession.insert(new Parent("carl", "eve"));
        ksession.insert(new Parent("tina", "eve"));
//
// parent
        ksession.insert(new Woman("mary"));
        ksession.insert(new Parent("carl", "mary"));
        ksession.insert(new Parent("tina", "mary"));
        ksession.insert(new Man("peter"));
        ksession.insert(new Parent("adam", "peter"));
        ksession.insert(new Parent("eve", "peter"));
        ksession.insert(new Man("paul"));
        ksession.insert(new Parent("adam", "paul"));
        ksession.insert(new Parent("mary", "paul"));
        ksession.insert(new Woman("jill"));
        ksession.insert(new Parent("adam", "jill"));
        ksession.insert(new Parent("eve", "jill"));
        ksession.fireAllRules();
    }
    @Test
    public void test() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");

        SessionPseudoClock clock=ksession.getSessionClock();

        ksession.insert(new FlightArrivalEvent(2L, clock.getCurrentTime()));
        int ruleCount= ksession.fireAllRules();
//        assertThat(ruleCount,equalTo(1));

        clock.advanceTime(5, TimeUnit.MINUTES);

        ksession.insert(new FlightArrivalEvent(1L, clock.getCurrentTime()));
        ruleCount= ksession.fireAllRules();
//        assertThat(ruleCount,equalTo(1));

//        clock.advanceTime(1, TimeUnit.MINUTES);
//        ksession.insert(new FlightArrivalEvent(1L));
//        int ruleCount= ksession.fireAllRules();
//        assertThat(ruleCount,equalTo(0));

    }
    @Test
    public void test2a() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");

        SessionPseudoClock clock=ksession.getSessionClock();

        ksession.insert(new TransactionEvent(2L, 1000.0));
//        int ruleCount= ksession.fireAllRules();
//        assertThat(ruleCount,equalTo(1));

        clock.advanceTime(10, TimeUnit.SECONDS);
        TransactionEvent te=new TransactionEvent(2L, 1000.0);
        ksession.insert(te);
//        assertThat(ruleCount,equalTo(1));
//        assertThat();
//        clock.advanceTime(1, TimeUnit.MINUTES);
//        ksession.insert(new FlightArrivalEvent(1L));
        int ruleCount= ksession.fireAllRules();
        assertThat(te.isDuplicate(), equalTo(true));
//        assertThat(ruleCount,equalTo(0));

    }
    @Test
    public void test2b() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("cepKsession");

        SessionPseudoClock clock=ksession.getSessionClock();

        Customer customer=new Customer(2L, 2100.0);
        ksession.insert(customer);

        TransactionEvent te1=new TransactionEvent(2L, 1000.0);
        ksession.insert(te1);
//        int ruleCount= ksession.fireAllRules();
//        assertThat(ruleCount,equalTo(1));

        clock.advanceTime(10, TimeUnit.SECONDS);
        TransactionEvent te2=new TransactionEvent(2L, 300.0);
        ksession.insert(te2);
//        assertThat(ruleCount,equalTo(1));
//        assertThat();
//        clock.advanceTime(1, TimeUnit.MINUTES);
//        ksession.insert(new FlightArrivalEvent(1L));
        TransactionEvent te3=new TransactionEvent(2L, 1000.0);
        ksession.insert(te3);
        int ruleCount= ksession.fireAllRules();
        assertThat(te1.isDuplicate(),equalTo(true));
        assertThat(te2.isDuplicate(),equalTo(false));
        assertThat(te3.isDuplicate(),equalTo(true));
//        assertThat(ruleCount,equalTo(0));

    }
}
