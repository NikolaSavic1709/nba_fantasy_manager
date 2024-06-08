package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.PlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerShortInfoDTO {
    Long id;
    String name;
    int fantasyPoints;
    String nationality;
    String nbaTeam;
    int position;
    PlayerStatus status;
    public PlayerShortInfoDTO(Player player)
    {
        this.id=player.getId();
        this.name=player.getName();
        this.fantasyPoints=player.getTotalFantasyPoints();
        this.nationality=player.getNationality();
        this.status=player.getStatus();
        this.nbaTeam=player.getNbaTeam().getName();
        this.position=player.getPosition().get(0);
    }

}
