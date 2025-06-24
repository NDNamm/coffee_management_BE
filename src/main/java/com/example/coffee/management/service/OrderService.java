package com.example.coffee.management.service;

import com.example.coffee.management.dto.OrderDTO;
import org.springframework.data.domain.Page;


public interface OrderService {

    //Admin
    Page<OrderDTO> getAllOrders(int page, int size);
    void updateOrder(OrderDTO orderDTO, Long orderId);
    void deleteOrder(Long orderId);
    Page<OrderDTO> searchOrderByKey(String key, int page, int size);

    //Cua User
    void addOrder(OrderDTO orderDTO, String email, String sessionId);
    Page<OrderDTO> getOrderClient(String email, String sessionId, int page, int size);
    void updateOrderByClient(OrderDTO orderDTO, Long orderId, String email, String sessionId);
    void cancelOrder(Long orderId, String email, String sessionId);
}
