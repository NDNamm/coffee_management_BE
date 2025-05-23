package com.example.demo.dto;

import com.example.demo.model.Enum.OrderStatus;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Users;
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
    private Users users;
    private List<OrderDetailDTO> orderDetailDTO;
    private String userName;
}
