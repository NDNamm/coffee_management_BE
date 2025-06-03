package com.example.demo.controller;

import com.example.demo.dto.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.Users;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartItemService;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO orderDTO,
                                      @RequestParam(required = false) String sessionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAnonymous = email == null || email.equals("anonymousUser");

        if (isAnonymous && sessionId != null) {
            orderService.addOrder(orderDTO, null, sessionId);
            cartService.deleteSession(sessionId);
        } else {
            orderService.addOrder(orderDTO, email, null);
            cartService.deleteCartByUser(email);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Da hang thanh cong");
    }

    @PutMapping("/update/{userId}/{orderId}")
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO orderDTO,
                                         @PathVariable Long userId,
                                         @PathVariable Long orderId,
                                         @RequestParam(required = false) String sessionId) {
        if (userId == 0) {
            userId = null;
        }
        orderService.updateOrder(orderDTO, userId, orderId, sessionId);
        return ResponseEntity.ok("Da update don hang thanh cong");
    }

    @DeleteMapping("delete/{userId}/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId,
                                         @PathVariable Long userId,
                                         @RequestParam(required = false) String sessionId) {
        if (userId == 0) {
            userId = null;
        }
        orderService.deleteOrder(userId, orderId, sessionId);
        return ResponseEntity.ok("Da delete don hang thanh cong");

    }

    @GetMapping("/search/{key}")
    public ResponseEntity<Page<OrderDTO>> searchOrder(@PathVariable String key,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "9") int size){
        return ResponseEntity.ok(orderService.searchOrderByKey(key, page, size));
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderDTO>> getOrderHistory( @RequestParam(required = false) String sessionId){
        String email = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            email = authentication.getName();
        }

        List<OrderDTO> orders = orderService.getOrder(email, sessionId);
        return ResponseEntity.ok(orders);
    }
}


