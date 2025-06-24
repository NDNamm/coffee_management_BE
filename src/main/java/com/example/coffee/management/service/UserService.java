package com.example.coffee.management.service;

import com.example.coffee.management.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserDTO> getAllUsers(int page, int size);
    void addUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO, Long id);
    void deleteUser(Long id);
    Page<UserDTO> selectUsers(int page, int size, String name);
}
