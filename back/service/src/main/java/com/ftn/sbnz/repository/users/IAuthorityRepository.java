package com.ftn.sbnz.repository.users;

import com.ftn.sbnz.model.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorityRepository extends JpaRepository<Authority,Integer> {
    Authority findByName(String name);
}