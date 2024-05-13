package com.ftn.sbnz.service;

import com.ftn.sbnz.model.models.Injury;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PlayerStatus;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
public class SampleHomeController {

	private final KieContainer kieContainer;

	@Autowired
    public SampleHomeController(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    @RequestMapping("/injury")
	public String injury() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kContainer = ks.getKieClasspathContainer();
//		KieSession kieSession = kContainer.newKieSession("fwKsession");
		KieSession kieSession = kieContainer.newKieSession("fwKsession");
		Player p = new Player();
		p.setId(1L);
		p.setStatus(PlayerStatus.HEALTHY);
		Injury i = new Injury(1L,"name", "desc", false, null, null, new Date(), p);
		kieSession.insert(i);
		kieSession.insert(p);
		kieSession.fireAllRules();
		kieSession.dispose();
		return "inj";
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
}
