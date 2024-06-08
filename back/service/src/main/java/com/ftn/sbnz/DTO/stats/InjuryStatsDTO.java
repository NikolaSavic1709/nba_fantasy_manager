package com.ftn.sbnz.DTO.stats;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InjuryStatsDTO {
    private String injuryName;
    private Long occurrence;
    private double averageRecoveryTime;
}
