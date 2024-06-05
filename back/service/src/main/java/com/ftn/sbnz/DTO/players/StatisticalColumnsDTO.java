package com.ftn.sbnz.DTO.players;

import com.ftn.sbnz.model.models.StatisticalColumns;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatisticalColumnsDTO {
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

    public StatisticalColumnsDTO(StatisticalColumns statisticalColumns) {
        this.ppg = statisticalColumns.getPpg();
        this.rpg = statisticalColumns.getRpg();
        this.apg = statisticalColumns.getApg();
        this.gp = statisticalColumns.getGp();
        this.mpg = statisticalColumns.getMpg();
        this.spg = statisticalColumns.getSpg();
        this.tpg = statisticalColumns.getTpg();
        this.bpg = statisticalColumns.getBpg();
        this.pfpg = statisticalColumns.getPfpg();
        this.fgPercentage = statisticalColumns.getFgPercentage();
        this.twoPointPercentage = statisticalColumns.getTwoPointPercentage();
        this.threePointPercentage = statisticalColumns.getThreePointPercentage();
    }
}
