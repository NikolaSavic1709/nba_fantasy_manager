package com.ftn.sbnz.DTO.players;

import com.ftn.sbnz.model.models.stats.FantasyStatisticalColumns;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FantasyStatisticalColumnsDTO {

    private int timesSelected;
    private int timesDropped;
    private int recommendationRank;

    public FantasyStatisticalColumnsDTO(FantasyStatisticalColumns fantasyStatisticalColumns) {
        this.timesSelected = fantasyStatisticalColumns.getTimesSelected();
        this.timesDropped = fantasyStatisticalColumns.getTimesDropped();
        this.recommendationRank = fantasyStatisticalColumns.getRecommendationRank();
    }
}
