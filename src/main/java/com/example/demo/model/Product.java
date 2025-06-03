package com.example.demo.model;

import com.example.demo.model.Enum.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "average_rating")
    private BigDecimal averageRating;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Categories category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Images> images;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Rating> rating;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CartItems> cartItem;


}
