package com.example.demo.dto;

import com.example.demo.model.Product;
import com.example.demo.model.Users;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
