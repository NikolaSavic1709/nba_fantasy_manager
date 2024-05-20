package com.ftn.sbnz.repository.players;

import com.ftn.sbnz.model.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepository extends JpaRepository<Player,Integer> {
}
