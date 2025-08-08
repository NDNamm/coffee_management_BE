package com.example.coffee.management.service;

import com.example.coffee.management.dto.AuthDTO;
import com.example.coffee.management.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthDTO login(UserDTO userDTO, HttpServletResponse response);
    void register(UserDTO userDTO);
    void logout(HttpServletResponse response);
    AuthDTO refresh(HttpServletRequest request, HttpServletResponse response);
}
