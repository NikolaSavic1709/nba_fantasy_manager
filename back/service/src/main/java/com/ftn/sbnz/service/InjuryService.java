package com.ftn.sbnz.service;

import com.ftn.sbnz.DTO.stats.InjuryStatsDTO;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.utils.KieSessionProvider;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InjuryService {

    private final KieContainer kieContainer;
    private final KieSessionProvider kieSessionProvider;

    @Autowired
    public InjuryService(KieContainer kieContainer, KieSessionProvider kieSessionProvider) {
        this.kieContainer = kieContainer;
        this.kieSessionProvider = kieSessionProvider;
    }

    public List<InjuryStatsDTO> getMostFrequentInjuryByThreshold(int threshold) {
        KieSession kieSession = kieSessionProvider.getKieSession();
        List<InjuryStatsDTO> result = new ArrayList<>();
        QueryResults queryResults = kieSession.getQueryResults("getFrequentInjuriesAndAverageRecovery", threshold);
        for (QueryResultsRow row : queryResults) {
            String injury = (String) row.get("$injuryName");
            Long occurrence = (Long) row.get("$occurrence");
            double avgRecoveryTime = (double) row.get("$avgRecoveryTime");
            result.add(new InjuryStatsDTO(injury, occurrence, avgRecoveryTime));
        }
        //kieSession.dispose();

        result.sort((i1, i2) -> Long.compare(
                i2.getOccurrence(),
                i1.getOccurrence()
        ));
        return result;
    }
}
