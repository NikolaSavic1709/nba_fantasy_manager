package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.models.FantasyTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("additionTime")
@Expires("7d")
public class PenaltyEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date additionTime;
    private FantasyTeam team;


    public PenaltyEvent(Long timestamp, FantasyTeam team) {
        this.additionTime=new Date(timestamp);
        this.team = team;
    }
}
