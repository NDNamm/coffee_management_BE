package com.example.coffee.management.dto;

import com.example.coffee.management.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesDTO {
    private Long id;

    @NotBlank(message = "CATEGORY_NAME_INVALID")
    @Size(min = 2, message = "CATEGORY_NAME_INVALID")
    private String nameCate;

    private String imageUrl;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Product> product;

}
