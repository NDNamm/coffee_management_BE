package com.example.demo.dto;

import com.example.demo.model.Enum.OrderStatus;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String name;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private User user;
    private List<OrderDetail> orderDetail;
}
