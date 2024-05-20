package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.models.FantasyTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFantasyTeamRepository extends JpaRepository<FantasyTeam,Integer> {
}
