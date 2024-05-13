package com.ftn.sbnz.model.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Injury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String description;
    private boolean isRecovered;
    private Integer recoveryTimeInDays;
    private Integer estimatedRecoveryTimeInDays;
    private Date timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player player;
}
