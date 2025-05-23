package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<OrderDTO> getAllOrders(int page, int size);
    void addOrder(OrderDTO orderDTO, Long userId);
    void updateOrder(OrderDTO orderDTO, Long userId, Long orderId);
    void deleteOrder(Long orderId);
//    Page<OrderDTO> getOrdersByUserId(Long userId, Pageable pageable);
}
