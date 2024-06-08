package com.ftn.sbnz.model.repository;

import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFantasyTeamRepository extends JpaRepository<FantasyTeam,Integer> {
    Optional<FantasyTeam> findById(Integer id);
}
