package com.example.demo.dto;

import com.example.demo.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class CartDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Users user;
    List<CartItemDTO> cartItems;
}
