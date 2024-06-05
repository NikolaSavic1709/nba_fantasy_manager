package com.ftn.sbnz.model.models.stats;

import com.ftn.sbnz.model.models.Player;
import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StatisticalColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(mappedBy = "statisticalColumns", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Player player;

    private double ppg;
    private double rpg;
    private double apg;
    private int gp;
    private double mpg;
    private double spg;
    private double tpg;
    private double bpg;
    private double pfpg;
    private double fgPercentage;
    private double twoPointPercentage;
    private double threePointPercentage;

}
