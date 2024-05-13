package com.ftn.sbnz.model.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String surname;
    private String nationality;

    @ElementCollection
    private List<Integer> position;

    private Date birthDate;

    private Integer totalFantasyPoints;
    private Integer totalBonusPoints;

    private Integer price;
    private PlayerStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private FantasyTeam fantasyTeam;

    @ManyToOne(fetch = FetchType.EAGER)
    private NBATeam nbaTeam;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Injury> injuries;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player playerStyle;

    @OneToOne(fetch = FetchType.EAGER)
    private StatisticalColumns statisticalColumns;

    @OneToOne(fetch = FetchType.EAGER)
    private FantasyStatisticalColumns fantasyStatisticalColumns;

}
