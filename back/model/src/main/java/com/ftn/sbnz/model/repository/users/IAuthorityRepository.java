package com.ftn.sbnz.model.repository.users;

import com.ftn.sbnz.model.models.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorityRepository extends JpaRepository<Authority,Integer> {
    Authority findByName(String name);
}