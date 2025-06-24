package com.example.coffee.management.controller;

import com.example.coffee.management.dto.ApiResponse;
import com.example.coffee.management.dto.UserDTO;
import com.example.coffee.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ApiResponse<Page<UserDTO>> getAllUser(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "6") int size) {
        ApiResponse<Page<UserDTO>> response = new ApiResponse<>();
        response.setData(userService.getAllUsers(page, size));
        return response;
    }

    @PostMapping("/add")
    public ApiResponse<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        userService.addUser(userDTO);
        response.setMessage("User added successfully");
        return response;
    }

    @PutMapping("update/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable("id") Long id,
                                        @RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        userService.updateUser(userDTO,id);
        response.setMessage("User update successfully");
        return response;
    }

    @DeleteMapping("delete/{id}")
    public ApiResponse<UserDTO> deleteUser(@PathVariable("id") Long id) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        userService.deleteUser(id);
        response.setMessage("User deleted successfully");
        return response;
    }

    @GetMapping("select/{key}")
    public ApiResponse<Page<UserDTO>> selectUser(@PathVariable("key") String key,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "6") int size) {
        ApiResponse<Page<UserDTO>> response = new ApiResponse<>();
        response.setData(userService.selectUsers(page, size,key));
        return response;
    }

}
