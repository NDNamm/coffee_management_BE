package com.example.coffee.management.controller;

import com.example.coffee.management.dto.ApiResponse;
import com.example.coffee.management.dto.AuthDTO;
import com.example.coffee.management.service.AuthService;
import com.example.coffee.management.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthDTO> login(@RequestBody UserDTO user, HttpServletResponse res) {
        ApiResponse<AuthDTO> response = new ApiResponse<>();
        response.setData(authService.login(user, res));
        return response;
    }

    @PostMapping("/register")
    public ApiResponse<AuthDTO> register(@RequestBody UserDTO user) {
        ApiResponse<AuthDTO> response = new ApiResponse<>();
        authService.register(user);
        response.setMessage("DK thanh cong");
        return response;
    }

    @PostMapping("/logout")
    public ApiResponse<AuthDTO> logout(HttpServletResponse res) {
        ApiResponse<AuthDTO> response = new ApiResponse<>();
        authService.logout(res);
        response.setMessage("Logged out successfully");
        return response;
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthDTO> refresh(HttpServletRequest req,HttpServletResponse res) {
        ApiResponse<AuthDTO> response = new ApiResponse<>();
        response.setData(authService.refresh(req, res));
        return response;
    }
}
