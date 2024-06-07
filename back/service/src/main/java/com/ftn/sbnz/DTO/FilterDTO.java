package com.ftn.sbnz.DTO;

import com.ftn.sbnz.model.models.Filter;
import com.ftn.sbnz.model.models.stats.CategoryScores;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilterDTO {
    private Long id;
    private Integer minPrice;
    private Integer maxPrice;
    private String team;
    private Integer position;

    public FilterDTO(Filter filter) {
        this.id = filter.getId();
        this.minPrice = filter.getMinPrice();
        this.maxPrice = filter.getMaxPrice();
        this.team = filter.getTeam();
        this.position = filter.getPosition();
    }
}
