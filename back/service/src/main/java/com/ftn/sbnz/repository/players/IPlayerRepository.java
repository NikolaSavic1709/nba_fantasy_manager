package com.ftn.sbnz.repository.players;

import com.ftn.sbnz.model.models.Administrator;
import com.ftn.sbnz.model.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPlayerRepository extends JpaRepository<Player,Integer> {
    Optional<Player> findByName(String name);
}
