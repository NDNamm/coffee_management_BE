package com.example.coffee.management.controller;

import com.example.coffee.management.dto.ApiResponse;
import com.example.coffee.management.dto.OrderDTO;
import com.example.coffee.management.service.CartService;
import com.example.coffee.management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    public static final String ANONYMOUS_USER = "anonymousUser";

    //Admin
    @GetMapping("")
    public ApiResponse<Page<OrderDTO>> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        ApiResponse<Page<OrderDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.getAllOrders(page, size));
        return apiResponse;
    }

    @PutMapping("/update/{orderId}")
    public ApiResponse<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO,
                                             @PathVariable Long orderId) {

        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>();
        orderService.updateOrder(orderDTO, orderId);
        apiResponse.setMessage("Đã sửa đơn hàng thành công");
        return apiResponse;
    }

    @DeleteMapping("delete/{orderId}")
    public ApiResponse<OrderDTO> deleteOrder(@PathVariable Long orderId) {

        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>();
        orderService.deleteOrder(orderId);
        apiResponse.setMessage("Đã xóa đơn hàng thành công");
        return apiResponse;

    }

    @GetMapping("/search/{key}")
    public ApiResponse<Page<OrderDTO>> searchOrder(@PathVariable String key,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "9") int size) {
        ApiResponse<Page<OrderDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.searchOrderByKey(key, page, size));
        return apiResponse;
    }

    //Client
    @PostMapping("/add")
    public ApiResponse<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO,
                                          @RequestParam(required = false) String sessionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAnonymous = email == null || email.equals(ANONYMOUS_USER);

        if (isAnonymous && sessionId != null) {
            orderService.addOrder(orderDTO, null, sessionId);
            cartService.deleteSession(sessionId);
        } else {
            orderService.addOrder(orderDTO, email, null);
            cartService.deleteCartByUser(email);
        }
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Đã sửa đơn hàng thành công");
        return apiResponse;
    }

    @PutMapping("/update_client/{orderId}")
    public ApiResponse<OrderDTO> updateClient(@RequestBody OrderDTO orderDTO,
                                              @PathVariable Long orderId,
                                              @RequestParam(required = false) String sessionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAnonymous = email == null || email.equals(ANONYMOUS_USER);

        if (isAnonymous && sessionId != null) {
            orderService.updateOrderByClient(orderDTO, orderId, null, sessionId);
        } else {
            orderService.updateOrderByClient(orderDTO, orderId, email, null);
        }
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Đã đặt hàng thành công");
        return apiResponse;
    }

    @PutMapping("/cancel/{orderId}")
    public ApiResponse<OrderDTO> cancelOrder(@PathVariable Long orderId,
                                             @RequestParam(required = false) String sessionId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAnonymous = email == null || email.equals(ANONYMOUS_USER);
        if (isAnonymous && sessionId != null) {
            orderService.cancelOrder(orderId, sessionId, email);
        } else {
            orderService.cancelOrder(orderId, null, email);
        }
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Đã hủy đơn hàng thành công");
        return apiResponse;
    }

    @GetMapping("/history")
    public ApiResponse<Page<OrderDTO>> getOrderHistory(@RequestParam(required = false) String sessionId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "9") int size) {

        String email = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !ANONYMOUS_USER.equals(authentication.getName())) {
            email = authentication.getName();
        }

        ApiResponse<Page<OrderDTO>> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.getOrderClient(email, sessionId, page, size));
        return apiResponse;
    }
}


