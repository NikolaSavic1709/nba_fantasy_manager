package com.ftn.sbnz.model.models;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Administrator extends User{
}
