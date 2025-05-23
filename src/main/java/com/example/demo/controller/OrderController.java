package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }
}
