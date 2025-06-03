package com.example.demo.dto;

import com.example.demo.model.Categories;
import com.example.demo.model.Enum.ProductStatus;
import com.example.demo.model.Images;
import com.example.demo.model.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private ProductStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private List<OrderDetail> orderDetails;
    private List<ImagesDTO> imagesDTO;
    private Long categoryId;
    private BigDecimal averageRating;
}
