package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Users> user = userRepository.findAll(pageable);

        return user.map(users -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(users.getId());
            userDTO.setEmail(users.getEmail());
            userDTO.setPassword(users.getPassword());
            userDTO.setCreatedAt(users.getCreatedAt());
            userDTO.setUpdateAt(users.getUpdateAt());
            return userDTO;
        });
    }

    @Override
    public void addUser(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Password Not Matched");
        }

        Users user = new Users();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDTO userDTO, Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tai khoan khong ton tai"));

        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tai khoan khong ton tai"));

        userRepository.delete(user);
    }

    @Override
    public Page<UserDTO> selectUsers(int page, int size, String key) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Users> user = userRepository.findUsersByFullNameOrEmail(key,pageable);

        return user.map(users -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(users.getId());
            userDTO.setEmail(users.getEmail());
            userDTO.setPassword(users.getPassword());
            userDTO.setCreatedAt(users.getCreatedAt());
            userDTO.setUpdateAt(users.getUpdateAt());
            return userDTO;
        });
    }
}
