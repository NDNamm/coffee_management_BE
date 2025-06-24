package com.example.coffee.management.service.impl;

import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.Role;
import com.example.coffee.management.dto.AuthDTO;
import com.example.coffee.management.dto.UserDTO;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.model.Users;
import com.example.coffee.management.repository.RoleRepository;
import com.example.coffee.management.repository.UserRepository;
import com.example.coffee.management.security.JwtUtils;
import com.example.coffee.management.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthDTO login(UserDTO userDTO) {
        Users user = userRepository.findUserByEmail(userDTO.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateToken(user.getEmail(), user.getRole().getRoleName());
            log.info("token: " + token);
            return new AuthDTO(token, user.getId(), user.getFullName(), user.getPhoneNumber(), user.getRole().getRoleName());
        } else {
            throw new UsernameNotFoundException("The username or password is incorrect");
        }

    }

    @Override
    public void register(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        Role role = roleRepository.findById(2L)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        Users user = Users.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .dob(userDTO.getDob())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .role(role)
                .build();
        userRepository.save(user);
    }
}
