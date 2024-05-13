package com.ftn.sbnz.model.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    private Integer minPrice;
    private Integer maxPrice;
    private String team;
    private Integer position;
    private Integer period;
}
