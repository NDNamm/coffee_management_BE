package com.example.coffee.management.service.impl;

import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.Role;
import com.example.coffee.management.service.UserService;
import com.example.coffee.management.dto.UserDTO;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.model.Users;
import com.example.coffee.management.repository.RoleRepository;
import com.example.coffee.management.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Users> userPage = userRepository.findAll(pageable);

        return userPage.map(user ->UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .createdAt(user.getCreatedAt())
                .updateAt(user.getUpdateAt())
                .build());
    }

    @Override
    public void addUser(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Role role = roleRepository.findById(3L)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if(userRepository.findUsersByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

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

    @Override
    public void updateUser(UserDTO userDTO, Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(userRepository.findUsersByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }

    @Override
    public Page<UserDTO> selectUsers(int page, int size, String key) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Users> usersPage = userRepository.findUsersByFullNameOrEmail(key,pageable);

        return usersPage.map(user -> UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .createdAt(user.getCreatedAt())
                .updateAt(user.getUpdateAt())
                .build());
    }
}
