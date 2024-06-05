package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.models.PlayerStatus;
import com.ftn.sbnz.model.models.Player;

import java.util.Date;
import java.util.List;

public class PlayerDetailsDTO {
    Long id;
    String name;
    int fantasyPoints;
    int bonusPoints;
    String nationality;
    String nbaTeam;
    List<Integer> position;
    PlayerStatus status;
    Date birthday;
    int price;
    double ppg;
    double rpg;
    double apg;
    double mpg;
    double spg;
    double tpg;
    double bpg;
    int gp;
    double pfpg;
    double fgPercentage;
    double twoPointPercentage;
    double threePointPercentage;
    
    public PlayerDetailsDTO(Player player)
    {
        this.id=player.getId();
        this.name=player.getName();
        this.fantasyPoints=player.getTotalFantasyPoints();
        this.bonusPoints=player.getTotalBonusPoints();
        this.nationality=player.getNationality();
        this.status=player.getStatus();
        this.nbaTeam=player.getNbaTeam().getName();
        this.position=player.getPosition();
        this.birthday=player.getBirthDate();
        this.price=player.getPrice();
        this.ppg=player.getStatisticalColumns().getPpg();
        this.apg=player.getStatisticalColumns().getApg();
        this.rpg=player.getStatisticalColumns().getRpg();
        this.spg=player.getStatisticalColumns().getSpg();
        this.bpg=player.getStatisticalColumns().getBpg();
        this.tpg=player.getStatisticalColumns().getTpg();
        this.mpg=player.getStatisticalColumns().getMpg();
        this.fgPercentage=player.getStatisticalColumns().getFgPercentage();
        this.twoPointPercentage=player.getStatisticalColumns().getTwoPointPercentage();
        this.threePointPercentage=player.getStatisticalColumns().getThreePointPercentage();
        this.gp=player.getStatisticalColumns().getGp();
        this.pfpg=player.getStatisticalColumns().getPfpg();
    }


}
