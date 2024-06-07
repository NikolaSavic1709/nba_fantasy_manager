package com.ftn.sbnz.model.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FilteredList {
    private List<Player> players;

    public FilteredList() {
        this.players = new ArrayList<>();
    }
}
