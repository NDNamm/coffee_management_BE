package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    Page<UserDTO> getAllUsers(int page, int size);
    void addUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO, Long id);
    void deleteUser(Long id);
    Page<UserDTO> selectUsers(int page, int size, String name);
}
