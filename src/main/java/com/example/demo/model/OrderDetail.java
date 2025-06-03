package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
