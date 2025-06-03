package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderDTO> getAllOrders(int page, int size);
    void updateOrder(OrderDTO orderDTO, Long userId, Long orderId, String sessionId);
    void deleteOrder(Long orderId, Long userId,String sessionId);
    Page<OrderDTO> searchOrderByKey(String key, int page, int size);

    //Cua User
    void addOrder(OrderDTO orderDTO, String email, String sessionId);
    List<OrderDTO> getOrder(String email, String sessionId);

}
