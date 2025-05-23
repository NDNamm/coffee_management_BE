package com.example.demo.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
