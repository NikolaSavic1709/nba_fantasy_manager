package com.ftn.sbnz.repository.players;

import com.ftn.sbnz.model.models.Injury;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInjuryRepository extends JpaRepository<Injury,Integer> {
}
