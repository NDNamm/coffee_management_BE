package com.example.demo.service;

import com.example.demo.dto.UserDTO;

public interface AuthService {
    String login(UserDTO userDTO);
    void register(UserDTO userDTO);
}
