package com.ftn.sbnz.controller;

import com.ftn.sbnz.DTO.players.PlayerDTO;
import com.ftn.sbnz.DTO.stats.CategoryScoresDTO;
import com.ftn.sbnz.model.models.Player;
import com.ftn.sbnz.model.models.RecommendationList;
import com.ftn.sbnz.model.models.stats.CategoryScores;
import com.ftn.sbnz.repository.ICategoryScoresRepository;
import com.ftn.sbnz.repository.players.IPlayerRepository;
import com.ftn.sbnz.utils.KieSessionProvider;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class CategoryScoresController {

    private final KieSessionProvider kieSessionProvider;
    private final IPlayerRepository playerRepository;
    private final ICategoryScoresRepository categoryScoresRepository;

    @Autowired
    public CategoryScoresController(KieContainer kieContainer, KieSessionProvider kieSessionProvider,
                                    IPlayerRepository playerRepository, ICategoryScoresRepository categoryScoresRepository) {
        this.kieSessionProvider = kieSessionProvider;
        this.playerRepository = playerRepository;
        this.categoryScoresRepository = categoryScoresRepository;
    }

    @GetMapping(value = "/category_scores")
    public ResponseEntity<?> getCategoryScores() {
        List<CategoryScores> categoryScores = categoryScoresRepository.findAll();
        List<CategoryScoresDTO> categoryScoresDTO = new ArrayList<>();
        for(CategoryScores c : categoryScores){
            categoryScoresDTO.add(new CategoryScoresDTO(c));
        }

        return new ResponseEntity<>(categoryScoresDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/category_scores/activate/{id}")
    public ResponseEntity<?> activateCategoryScores(@PathVariable Integer id) {
        List<CategoryScores> categoryScoresList = categoryScoresRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        for (CategoryScores score : categoryScoresList) {
            score.setActive(false);
        }

        CategoryScores categoryScoreToActivate = categoryScoresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoryScores not found with id " + id));
        categoryScoreToActivate.setActive(true);

        categoryScoresRepository.saveAll(categoryScoresList);

        return new ResponseEntity<>(new CategoryScoresDTO(categoryScoreToActivate), HttpStatus.OK);
    }
}

