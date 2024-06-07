package com.ftn.sbnz.DTO.stats;

import com.ftn.sbnz.model.models.stats.CategoryScores;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddCategoryScoresDTO {
    private Integer pointScore;
    private Integer reboundScore;
    private Integer assistScore;
    private Integer stealScore;
    private Integer turnoverScore;
    private Integer blockScore;
    private Integer bonusMargin;

    public AddCategoryScoresDTO(CategoryScores categoryScores) {
        this.pointScore = categoryScores.getPointScore();
        this.reboundScore = categoryScores.getReboundScore();
        this.assistScore = categoryScores.getAssistScore();
        this.stealScore = categoryScores.getStealScore();
        this.turnoverScore = categoryScores.getTurnoverScore();
        this.blockScore = categoryScores.getBlockScore();
        this.bonusMargin = categoryScores.getBonusMargin();
    }

    public CategoryScores generateCategoryScores() {
        CategoryScores categoryScores = new CategoryScores();
        categoryScores.setPointScore(pointScore);
        categoryScores.setReboundScore(reboundScore);
        categoryScores.setAssistScore(assistScore);
        categoryScores.setStealScore(stealScore);
        categoryScores.setTurnoverScore(turnoverScore);
        categoryScores.setBlockScore(blockScore);
        categoryScores.setBonusMargin(bonusMargin);
        return categoryScores;
    }
}
