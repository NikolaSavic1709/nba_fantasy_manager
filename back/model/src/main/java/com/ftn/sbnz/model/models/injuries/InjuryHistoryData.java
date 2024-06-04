package com.ftn.sbnz.model.models.injuries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.type.Position;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InjuryHistoryData {
    int id;
    String description;
    double totalDays;
    int injuryCount;
}
