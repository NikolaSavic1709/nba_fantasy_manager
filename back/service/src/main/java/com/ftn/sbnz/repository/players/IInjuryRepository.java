package com.ftn.sbnz.repository.players;

import com.ftn.sbnz.model.models.injuries.Injury;
import com.ftn.sbnz.model.models.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IInjuryRepository extends JpaRepository<Injury,Integer> {
    Optional<Injury> findById(Integer id);
}
