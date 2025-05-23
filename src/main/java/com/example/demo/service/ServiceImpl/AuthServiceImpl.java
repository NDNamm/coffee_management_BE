package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String login(UserDTO userDTO) {
        Users user = userRepository.findUserByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email Not Found"));

        if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateToken(user.getEmail(), user.getRole().getName());
            System.out.println("token: " + token);
            return token;
        } else {
            throw new UsernameNotFoundException("tk hoac mk sai");
        }

    }

    @Override
    public void register(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new RuntimeException("Password Not Matched");
        }

        Role role = roleRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Role Not Found"));

        Users user = new Users();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        user.setRole(role);
        userRepository.save(user);
    }
}
