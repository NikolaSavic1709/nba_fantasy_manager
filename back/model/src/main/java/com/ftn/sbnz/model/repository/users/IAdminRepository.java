package com.ftn.sbnz.model.repository.users;

import com.ftn.sbnz.model.models.user.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAdminRepository extends JpaRepository<Administrator,Integer> {
    Optional<Administrator> findByEmail(String email);

    Optional<Administrator> findById(Integer id);
}
