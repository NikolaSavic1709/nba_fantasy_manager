package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.models.stats.CategoryScores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryScoresRepository extends JpaRepository<CategoryScores,Integer> {
}
