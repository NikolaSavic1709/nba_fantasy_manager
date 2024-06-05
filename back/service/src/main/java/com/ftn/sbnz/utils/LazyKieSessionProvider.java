package com.ftn.sbnz.utils;

import com.ftn.sbnz.model.models.Filter;
import com.ftn.sbnz.repository.IFilterRepository;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LazyKieSessionProvider implements KieSessionProvider {

    @Autowired
    private TemplateLoader templateLoader;

    @Autowired
    private IFilterRepository filterRepository;

    private KieSession kieSession;

    @Override
    public synchronized KieSession getKieSession() {
        if (kieSession == null) {
            if (dataIsLoaded()) {
                //KieHelper kieHelper = TemplateLoader.loadFromSpreadsheets();
                KieHelper kieHelper = templateLoader.loadFromObjects();
                kieSession = kieHelper.build().newKieSession();
            } else {
                throw new RuntimeException("Data from data.sql is not loaded yet");
            }
        }
        return kieSession;
    }

    private boolean dataIsLoaded() {
        List<Filter> data = filterRepository.findAll();
        return !data.isEmpty();
    }
}
