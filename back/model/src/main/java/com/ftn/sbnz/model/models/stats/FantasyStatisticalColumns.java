package com.ftn.sbnz.model.models.stats;

import com.ftn.sbnz.model.models.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Random;

@Data
@AllArgsConstructor
@Entity
public class FantasyStatisticalColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(mappedBy = "fantasyStatisticalColumns", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Player player;

    private int timesSelected;
    private int timesDropped;
    private int recommendationRank;

    public FantasyStatisticalColumns(){
        this.timesSelected = getRandom();
        this.timesDropped = getRandom();
        this.recommendationRank = 0;
    }

    public FantasyStatisticalColumns(int timesSelected, int timesDropped, int recommendationRank){
        this.timesSelected = timesSelected;
        this.timesDropped = timesDropped;
        this.recommendationRank = recommendationRank;
    }

    private int getRandom() {
        double num = Math.random();

        if (num > 0.99) return 5;
        if (num > 0.98) return 4;
        if (num > 0.95) return 3;
        if (num > 0.92) return 2;
        if (num > 0.9) return 1;
        return 0;
    }
}
