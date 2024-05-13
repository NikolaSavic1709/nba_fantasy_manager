package com.ftn.sbnz.model.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Manager extends User{

    @OneToOne(fetch = FetchType.EAGER)
    private FantasyTeam team;
}
