package com.example.demo.service;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.model.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderDetailService  {
    List<OrderDetailDTO> findOrderDetailByOrderId(Long orderId);
}
