package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PreferencesList;
import com.ftn.sbnz.model.models.RecommendationList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationListDTO {

    private List<PlayerShortInfoDTO> playersRecommendation;
    private List<PlayerShortInfoDTO> playersPreferences;
    public RecommendationListDTO(RecommendationList recommendationList, PreferencesList preferencesList, FantasyTeam removeFromThis){
        this.playersRecommendation=recommendationList.getPlayers().stream()
                .filter(player -> player.getFantasyTeam() == null)
                .limit(10)
                .map(PlayerShortInfoDTO::new)
                .collect(Collectors.toList());
        this.playersPreferences=preferencesList.getPlayers().stream().limit(10)
                .map(PlayerShortInfoDTO::new)
                .collect(Collectors.toList());
    }
}
