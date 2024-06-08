package com.ftn.sbnz.model.repository.players;

import com.ftn.sbnz.model.models.stats.StatisticalColumns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStatisticalColumnsRepository extends JpaRepository<StatisticalColumns,Integer> {
}
