package com.ftn.sbnz.DTO.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JWTToken {
    private String accessToken;
    private int expiresIn;
}
