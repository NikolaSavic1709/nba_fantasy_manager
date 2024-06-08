package com.ftn.sbnz.model.models.injuries;

import com.ftn.sbnz.model.models.Player;
import lombok.*;

import jakarta.persistence.*;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class Injury implements Serializable {

    private static final long serialVersionUID = 1L;

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
