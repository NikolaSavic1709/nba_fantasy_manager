package com.ftn.sbnz.model.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
