package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.user.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fantasy_team")
public class FantasyTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "fantasyTeam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Player> players;
}
