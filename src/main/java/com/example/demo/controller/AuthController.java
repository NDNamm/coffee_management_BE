package com.example.demo.controller;

import com.example.demo.dto.AuthDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Users;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody UserDTO user) {
        AuthDTO authDTO = authService.login(user);
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO user) {
        authService.register(user);
        return ResponseEntity.ok().body("Register success");
    }
}
