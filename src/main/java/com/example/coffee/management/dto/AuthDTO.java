package com.example.coffee.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthDTO {
    private String token;
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private String nameRole;
    private String refreshToken;
}
