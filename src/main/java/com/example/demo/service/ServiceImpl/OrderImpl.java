package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderDetailDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Page<OrderDTO> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = orderRepository.findAll(pageable);

        return orders.map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setName(order.getName());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            List<OrderDetailDTO> orderDetailsDTO = new ArrayList<>();
            if (order.getOrderDetail() != null) {
                orderDetailsDTO = order.getOrderDetail().stream().map(
                        orderDetail -> {
                            OrderDetailDTO detailDTO = new OrderDetailDTO();
                            detailDTO.setId(orderDetail.getId());
                            detailDTO.setProductName(orderDetail.getProduct().getName());
                            detailDTO.setPrice(orderDetail.getPrice());
                            detailDTO.setQuantity(orderDetail.getQuantity());
                            return detailDTO;
                        }
                ).collect(Collectors.toList());
            } else {
                new RuntimeException("Chua co don dang");
            }
            dto.setOrderDetailDTO(orderDetailsDTO);
            dto.setOrderDate(order.getOrderDate());
            dto.setUserName(order.getUser().getFullName());
            return dto;
        });
    }

    @Override
    public void addOrder(OrderDTO orderDTO, Long userId) {

    }

    @Override
    public void updateOrder(OrderDTO orderDTO, Long userId, Long orderId) {

    }

    @Override
    public void deleteOrder(Long orderId) {

    }
}
