package com.example.coffee.management.service;

import com.example.coffee.management.dto.AuthDTO;
import com.example.coffee.management.dto.UserDTO;

public interface AuthService {
    AuthDTO login(UserDTO userDTO);
    void register(UserDTO userDTO);
}
