package com.example.demo.service;

import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.UserDTO;

public interface AuthService {
    AuthDTO login(UserDTO userDTO);
    void register(UserDTO userDTO);
}
