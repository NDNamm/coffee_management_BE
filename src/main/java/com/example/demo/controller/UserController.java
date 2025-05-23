package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<Page<UserDTO>> getAllUser(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
                                        @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO, id);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("select/{key}")
    public ResponseEntity<Page<UserDTO>> selectUser(@PathVariable("key") String key,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(userService.selectUsers(page, size, key));
    }

}
