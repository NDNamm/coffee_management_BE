package com.example.coffee.management.controller;

import com.example.coffee.management.dto.ApiResponse;
import com.example.coffee.management.dto.AuthDTO;
import com.example.coffee.management.service.AuthService;
import com.example.coffee.management.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthDTO> login(@RequestBody UserDTO user) {
        ApiResponse<AuthDTO> response = new ApiResponse<>();
        response.setData(authService.login(user));
        return response;
    }

    @PostMapping("/register")
    public ApiResponse<AuthDTO> register(@RequestBody UserDTO user) {
        ApiResponse<AuthDTO> response = new ApiResponse<>();
        authService.register(user);
        response.setMessage("DK thanh cong");
        return response;
    }
}
