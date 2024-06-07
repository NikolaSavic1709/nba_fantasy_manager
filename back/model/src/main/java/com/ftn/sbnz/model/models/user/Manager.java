package com.ftn.sbnz.model.models.user;

import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Manager extends User {

    @OneToOne(fetch = FetchType.EAGER)
    private FantasyTeam team;

}
