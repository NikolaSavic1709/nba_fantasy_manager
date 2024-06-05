package com.ftn.sbnz.DTO.stats;

import com.ftn.sbnz.model.models.stats.CategoryScores;
import com.ftn.sbnz.model.models.stats.StatisticalColumns;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryScoresDTO {
    private Long id;
    private Integer pointScore;
    private Integer reboundScore;
    private Integer assistScore;
    private Integer stealScore;
    private Integer turnoverScore;
    private Integer blockScore;
    private Integer bonusMargin;
    private Boolean isActive;

    public CategoryScoresDTO(CategoryScores categoryScores) {
        this.id = categoryScores.getId();
        this.pointScore = categoryScores.getPointScore();
        this.reboundScore = categoryScores.getReboundScore();
        this.assistScore = categoryScores.getAssistScore();
        this.stealScore = categoryScores.getStealScore();
        this.turnoverScore = categoryScores.getTurnoverScore();
        this.blockScore = categoryScores.getBlockScore();
        this.bonusMargin = categoryScores.getBonusMargin();
        this.isActive = categoryScores.isActive();
    }
}
