package com.ftn.sbnz.service;

import com.ftn.sbnz.DTO.TeamSelectionDTO;
import com.ftn.sbnz.model.models.NBATeam;
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
public class SelectionService {

    private final KieContainer kieContainer;
    private final KieSessionProvider kieSessionProvider;

    @Autowired
    public SelectionService(KieContainer kieContainer, KieSessionProvider kieSessionProvider) {
        this.kieContainer = kieContainer;
        this.kieSessionProvider = kieSessionProvider;
    }

    public List<Player> getMostSelectedPlayersByThreshold(int threshold) {
        KieSession kieSession = kieSessionProvider.getKieSession();
        List<Player> result = new ArrayList<>();
        QueryResults queryResults = kieSession.getQueryResults("getMostSelectedPlayersByThreshold", threshold);
        for (QueryResultsRow row : queryResults) {
            Player player = (Player) row.get("$player");
            result.add(player);
        }
        //kieSession.dispose();

        result.sort((p1, p2) -> Integer.compare(
                p2.getFantasyStatisticalColumns().getTimesSelected(),
                p1.getFantasyStatisticalColumns().getTimesSelected()
        ));
        return result;
    }

    public List<Player> getMostSelectedPlayersByNBATeamName(String team) {
        KieSession kieSession = kieSessionProvider.getKieSession();
        List<Player> result = new ArrayList<>();
        QueryResults queryResults = kieSession.getQueryResults("getMostSelectedPlayersByNBATeamName", team);
        for (QueryResultsRow row : queryResults) {
            Player player = (Player) row.get("$player");
            result.add(player);
        }
        //kieSession.dispose();

        result.sort((p1, p2) -> Integer.compare(
                p2.getFantasyStatisticalColumns().getTimesSelected(),
                p1.getFantasyStatisticalColumns().getTimesSelected()
        ));
        return result;
    }

    public List<TeamSelectionDTO> getTeamsWithSelectedPlayers() {
        KieSession kieSession = kieSessionProvider.getKieSession();
        List<TeamSelectionDTO> result = new ArrayList<>();
        QueryResults queryResults = kieSession.getQueryResults("getTeamsWithSelectedPlayers");
        for (QueryResultsRow row : queryResults) {
            NBATeam team = (NBATeam) row.get("$team");
            Long timesSelected = (Long) row.get("$count");
            result.add(new TeamSelectionDTO(team.getName(), timesSelected));
        }

        result.sort((t1, t2) -> Long.compare(
                t2.getTimesSelected(),
                t1.getTimesSelected()
        ));
        //kieSession.dispose();
        return result;
    }
}
