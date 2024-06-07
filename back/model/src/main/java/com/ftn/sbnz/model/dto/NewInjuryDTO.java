package com.ftn.sbnz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewInjuryDTO {
    Long playerId;
    List<String> name;
    String description;

}
