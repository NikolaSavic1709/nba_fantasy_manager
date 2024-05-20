package com.ftn.sbnz.model.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategoryScores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private Integer pointScore;
    private Integer reboundScore;
    private Integer assistScore;
    private Integer stealScore;
    private Integer turnoverScore;
    private Integer blockScore;
    private Integer bonusMargin;
}
