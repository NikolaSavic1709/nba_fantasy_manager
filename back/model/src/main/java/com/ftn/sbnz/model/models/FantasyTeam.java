package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.events.PlayerAdditionEvent;
import com.ftn.sbnz.model.models.user.Manager;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fantasy_team")
public class FantasyTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    @OneToOne(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "fantasyTeam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Player> players;

    private int totalPoints;

}
