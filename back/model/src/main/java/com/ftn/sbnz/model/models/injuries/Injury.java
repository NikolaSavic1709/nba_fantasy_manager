package com.ftn.sbnz.model.models.injuries;

import com.ftn.sbnz.model.models.Player;
import lombok.*;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Injury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ElementCollection
    private List<String> name;
    private String description;
    private boolean isRecovered;
    private Integer recoveryTimeInDays;
    private Integer estimatedRecoveryTimeInDays;
    private Date timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player player;
}
