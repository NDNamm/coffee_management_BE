package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.OrderDetailDTO;
import com.example.coffee.management.model.OrderDetail;
import com.example.coffee.management.repository.OrderDetailRepository;
import com.example.coffee.management.service.OrderDetailService;
import com.example.coffee.management.model.Order;
import com.example.coffee.management.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OderDetailImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDetailDTO> findOrderDetailByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Don hang k ton tai"));

        List<OrderDetail> orderDetails = order.getOrderDetail();
        List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setId(orderDetail.getId());
            dto.setProductName(orderDetail.getProduct().getNamePro());
            dto.setUrlProductImage(orderDetail.getProduct().getImageUrl());
            dto.setPrice(orderDetail.getPrice());
            dto.setQuantity(orderDetail.getQuantity());
            dto.setTotalPrice(orderDetail.getTotalPrice());

            orderDetailDTOs.add(dto);

        }

        return orderDetailDTOs;
    }
}
