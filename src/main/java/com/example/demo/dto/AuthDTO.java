package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    private String token;
    private Long userId;
    private String fullName;
    private String nameRole;
    private String phone;

    public AuthDTO(String token, Long userId, String fullName, String phone, String nameRole) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.nameRole = nameRole;

    }
}
