package com.ftn.sbnz.model.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationList {

    private List<Player> players;
}
