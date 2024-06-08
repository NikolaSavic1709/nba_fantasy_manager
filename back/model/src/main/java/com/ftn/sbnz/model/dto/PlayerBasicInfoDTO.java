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
public class PlayerBasicInfoDTO {
    Long id;
    String name;
    public PlayerBasicInfoDTO(Player player)
    {
        this.id=player.getId();
        this.name=player.getName();
    }

}