package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Role(Role.Type.EVENT)
@Timestamp("additionTime")
public class PlayerAdditionEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date additionTime;
    private Player player;
    private FantasyTeam team;


    public PlayerAdditionEvent(Long timestamp, Player player, FantasyTeam team) {
        this.additionTime=new Date(timestamp);
        this.player = player;
        this.team = team;
    }

}
