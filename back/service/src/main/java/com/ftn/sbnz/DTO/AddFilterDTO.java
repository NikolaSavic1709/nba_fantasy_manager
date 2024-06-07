package com.ftn.sbnz.DTO;

import com.ftn.sbnz.model.models.Filter;
import com.ftn.sbnz.model.models.stats.CategoryScores;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddFilterDTO {
    private Integer minPrice;
    private Integer maxPrice;
    private String team;
    private Integer position;

    public AddFilterDTO(Filter filter) {
        this.minPrice = filter.getMinPrice();
        this.maxPrice = filter.getMaxPrice();
        this.team = filter.getTeam();
        this.position = filter.getPosition();
    }

    public Filter generateFilter() {
        Filter filter = new Filter();
        filter.setMinPrice(this.minPrice);
        filter.setMaxPrice(this.maxPrice);
        filter.setTeam(this.team);
        filter.setPosition(this.position);
        return filter;
    }
}
