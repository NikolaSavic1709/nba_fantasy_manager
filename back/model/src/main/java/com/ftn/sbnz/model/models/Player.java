package com.ftn.sbnz.model.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIgnoreProperties("nbaTeam")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
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


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Player player = (Player) o;
//        return Objects.equals(id, player.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }

}
