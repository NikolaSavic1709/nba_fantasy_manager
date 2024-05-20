package com.ftn.sbnz.repository.users;

import com.ftn.sbnz.model.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer id);
}
