package com.ftn.sbnz.model.repository;

import com.ftn.sbnz.model.models.NBATeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INBATeamRepository extends JpaRepository<NBATeam,Integer> {
}
