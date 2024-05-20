package com.ftn.sbnz.repository;

import com.ftn.sbnz.model.models.CategoryScores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryScoresRepository extends JpaRepository<CategoryScores,Integer> {
}
