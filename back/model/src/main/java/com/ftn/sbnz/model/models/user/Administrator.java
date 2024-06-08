package com.ftn.sbnz.model.models.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Administrator extends User{
}
