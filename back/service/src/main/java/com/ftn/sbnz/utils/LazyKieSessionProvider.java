package com.ftn.sbnz.utils;

import com.ftn.sbnz.model.models.Filter;
import com.ftn.sbnz.model.repository.IFantasyTeamRepository;
import com.ftn.sbnz.model.repository.IFilterRepository;
import com.ftn.sbnz.model.repository.INBATeamRepository;
import com.ftn.sbnz.model.repository.players.IInjuryRepository;
import com.ftn.sbnz.model.repository.players.IPlayerRepository;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LazyKieSessionProvider implements KieSessionProvider {

    @Autowired
    private TemplateLoader templateLoader;

    @Autowired
    private IFilterRepository filterRepository;

    @Autowired
    private IPlayerRepository playerRepository;
    @Autowired
    private IInjuryRepository injuryRepository;
    @Autowired
    private INBATeamRepository nbaTeamRepository;
    @Autowired
    private IFantasyTeamRepository fantasyTeamRepository;

    private KieSession kieSession;

    @Override
    public KieSession getKieSession() {
        if (kieSession == null) {
            initializeKieSession();
        }
        return kieSession;
    }

    @Override
    public void refreshKieSession() {
        if (kieSession != null) {
            List<Object> facts = extractFacts(kieSession);
            kieSession.destroy();
            initializeKieSession();
            insertFacts(kieSession, facts);
            setGlobals(kieSession);
        } else {
            initializeKieSession();
        }
    }

    private void setGlobals(KieSession kieSession){
        kieSession.setGlobal("playerRepository", playerRepository);
        kieSession.setGlobal("injuryRepository", injuryRepository);
        kieSession.setGlobal("fantasyTeamRepository", fantasyTeamRepository);
    }

    private void initializeKieSession() {
        if (dataIsLoaded()) {
            KieServices kieServices = KieServices.Factory.get();
            KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
            kieBaseConfiguration.setOption(EventProcessingOption.STREAM);
            KieHelper kieHelper = templateLoader.loadFromObjects();
            kieSession = kieHelper.build(kieBaseConfiguration).newKieSession();
            TemplateLoader.getNumberOfRules(kieSession);
        } else {
            throw new RuntimeException("Data from data.sql is not loaded yet");
        }
    }

    private boolean dataIsLoaded() {
        List<Filter> data = filterRepository.findAll();
        return !data.isEmpty();
    }

    private List<Object> extractFacts(KieSession kieSession) {
        List<Object> facts = new ArrayList<>();
        for (Object fact : kieSession.getObjects()) {
            facts.add(fact);
        }
        return facts;
    }

    private void insertFacts(KieSession kieSession, List<Object> facts) {
        for (Object fact : facts) {
            kieSession.insert(fact);
        }
    }
}
