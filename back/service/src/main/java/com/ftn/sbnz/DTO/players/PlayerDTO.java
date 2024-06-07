package com.ftn.sbnz.DTO.players;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftn.sbnz.model.models.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerDTO {
    private Long id;
    private String name;
    private String nationality;
    private List<Integer> position;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private Date birthDate;

    private Integer totalFantasyPoints;
    private Integer totalBonusPoints;

    private Integer price;
    private PlayerStatus status;

    private String nbaTeam;

    private StatisticalColumnsDTO statisticalColumns;

    private FantasyStatisticalColumnsDTO fantasyStatisticalColumns;

    public PlayerDTO(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.nationality = player.getNationality();
        this.position = player.getPosition();
        this.birthDate = player.getBirthDate();
        this.totalFantasyPoints = player.getTotalFantasyPoints();
        this.totalBonusPoints = player.getTotalBonusPoints();
        this.price = player.getPrice();
        this.status = player.getStatus();

        this.nbaTeam = player.getNbaTeam().getName();

        this.statisticalColumns = new StatisticalColumnsDTO(player.getStatisticalColumns());
        this.fantasyStatisticalColumns = new FantasyStatisticalColumnsDTO(player.getFantasyStatisticalColumns());
        //this.fantasyStatisticalColumns = null;
    }
}
