package com.example.coffee.management.dto;

import com.example.coffee.management.model.enums.ProductStatus;
import com.example.coffee.management.model.OrderDetail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;

    @NotBlank(message = "PRODUCT_NAME_INVALID")
    @Size(min = 2, message = "PRODUCT_NAME_INVALID")
    private String namePro;

    private String imageUrl;

    @NotBlank(message = "PRODUCT_PRICE_INVALID")
    private BigDecimal price;

    @NotBlank(message = "PRODUCT_STATUS_INVALID")
    private ProductStatus status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private List<OrderDetail> orderDetails;
    private List<ImagesDTO> imagesDTO;
    private Long categoryId;
    private BigDecimal averageRating;
}
