package com.ftn.sbnz.model.repository;

import com.ftn.sbnz.model.models.FantasyTeam;
import com.ftn.sbnz.model.models.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFilterRepository extends JpaRepository<Filter,Integer> {
}
