package com.ftn.sbnz.repository.users;

import com.ftn.sbnz.model.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IManagerRepository extends JpaRepository<Manager,Integer> {
    Optional<Manager> findByEmail(String email);

    Optional<Manager> findById(Integer id);

}