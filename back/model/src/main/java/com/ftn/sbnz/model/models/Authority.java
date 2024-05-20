package com.ftn.sbnz.model.models;

import lombok.Getter;
import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority {
//public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    public Authority() {
        super();
    }

    public Authority(String name) {
        this.setName(name);
    }

//    @Override
//    public String getAuthority() {
//        return this.name;
//    }
}
