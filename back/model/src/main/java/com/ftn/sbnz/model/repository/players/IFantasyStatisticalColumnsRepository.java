package com.ftn.sbnz.model.repository.players;

import com.ftn.sbnz.model.models.stats.FantasyStatisticalColumns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFantasyStatisticalColumnsRepository extends JpaRepository<FantasyStatisticalColumns,Integer> {
}
