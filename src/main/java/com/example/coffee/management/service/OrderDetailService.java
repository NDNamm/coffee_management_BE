package com.example.coffee.management.service;

import com.example.coffee.management.dto.OrderDetailDTO;

import java.util.List;

public interface OrderDetailService  {
    List<OrderDetailDTO> findOrderDetailByOrderId(Long orderId);
}
