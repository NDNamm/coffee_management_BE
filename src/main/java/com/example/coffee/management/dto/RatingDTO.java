package com.example.coffee.management.dto;

import com.example.coffee.management.model.Product;
import com.example.coffee.management.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDTO {
    private int ratingValue;
    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private Product product;
    private Users user;
    private String userName;
    private Long productId;
    private Long userId;

}
