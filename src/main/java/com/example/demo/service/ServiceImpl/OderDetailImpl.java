package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderDetailService;
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
            dto.setProductName(orderDetail.getProduct().getName());
            dto.setUrlProductImage(orderDetail.getProduct().getImageUrl());
            dto.setPrice(orderDetail.getPrice());
            dto.setQuantity(orderDetail.getQuantity());
            dto.setTotalPrice(orderDetail.getTotalPrice());

            orderDetailDTOs.add(dto);

        }

        return orderDetailDTOs;
    }
}
