package com.example.coffee.management.controller;

import com.example.coffee.management.dto.OrderDetailDTO;
import com.example.coffee.management.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/orderDetail")
public class OderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getAllOrderDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderDetailService.findOrderDetailByOrderId(orderId));
    }


}
