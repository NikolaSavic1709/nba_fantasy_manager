package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.models.injuries.Injury;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class InjuryDTO {
    Long id;
    String player;
    String team;
    String description;
    boolean isRecovered;
    Integer recoveryTimeInDays;
    Integer estimatedRecoveryTimeInDays;
    Date timestamp;
    public InjuryDTO(Injury injury)
    {
        this.id=injury.getId();
        this.player=injury.getPlayer().getName();
        this.team=injury.getPlayer().getNbaTeam().getName();
        this.description=injury.getDescription();
        this.isRecovered=injury.isRecovered();
        this.recoveryTimeInDays=injury.getRecoveryTimeInDays();
        this.estimatedRecoveryTimeInDays=injury.getEstimatedRecoveryTimeInDays();
        this.timestamp=injury.getTimestamp();
    }
}
