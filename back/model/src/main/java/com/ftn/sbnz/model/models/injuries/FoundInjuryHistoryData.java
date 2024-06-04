package com.ftn.sbnz.model.models.injuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FoundInjuryHistoryData {
    String description;
    double totalDays;
    int injuryCount;
    Injury injury;
}

