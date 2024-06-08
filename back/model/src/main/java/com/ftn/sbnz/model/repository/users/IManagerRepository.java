package com.ftn.sbnz.model.repository.users;

import com.ftn.sbnz.model.models.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IManagerRepository extends JpaRepository<Manager,Integer> {
    Optional<Manager> findByEmail(String email);

    Optional<Manager> findById(Integer id);

}
