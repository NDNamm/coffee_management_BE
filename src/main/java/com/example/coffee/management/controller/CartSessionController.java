package com.example.coffee.management.controller;

import com.example.coffee.management.dto.ApiResponse;
import com.example.coffee.management.dto.CartDTO;
import com.example.coffee.management.dto.CartItemDTO;
import com.example.coffee.management.model.Carts;
import com.example.coffee.management.service.CartService;
import com.example.coffee.management.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/cart/session")
@RequiredArgsConstructor
public class CartSessionController {

    private final CartService cartService;
    private final CartRepository cartRepository;

    @PostMapping("/create")
    public ApiResponse<Carts> createSessionCart() {
        String sessionId = UUID.randomUUID().toString();

        Carts carts = new Carts();
        carts.setSessionId(sessionId);
        carts.setCreatedAt(LocalDateTime.now());
        cartRepository.save(carts);

        ApiResponse<Carts> response = new ApiResponse<>();
        response.setMessage("Tạo sessionId thành công");
        response.setData(carts);
        return response;
    }

    //Gio hang Ao
    @GetMapping("")
    public ApiResponse<CartDTO> getCartNySession(@RequestParam String sessionId) {
        ApiResponse<CartDTO> response = new ApiResponse<>();
        response.setData(cartService.getCartBySession(sessionId));
        return response;
    }


    @PostMapping("/add")
    public ApiResponse<CartDTO> addCartSession(@RequestBody CartItemDTO cartItemDTO,
                                               @RequestParam String sessionId) {
        ApiResponse<CartDTO> response = new ApiResponse<>();
        cartService.addSession(sessionId, cartItemDTO);
        response.setMessage("Đã thêm sản phẩm vào giỏ hàng");
        return response;
    }

    //Gop gio hang vao khi dang nhap
    @PostMapping("/merge")
    public ApiResponse<CartDTO> mergeCartSession(@RequestParam String sessionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse<CartDTO> response = new ApiResponse<>();
        cartService.mergeSession(sessionId, email);
        response.setMessage("Đã gộp giỏ hàng vào giỏ hàng người dùng");
        return response;
    }

    @PutMapping("/update")
    public ApiResponse<CartDTO> updateCartSession(@RequestBody CartItemDTO cartItemDTO, @RequestParam String sessionId) {
        ApiResponse<CartDTO> response = new ApiResponse<>();
        cartService.updateCartBySession(sessionId, cartItemDTO);
        response.setMessage("Đã sửa sản phẩm trong giỏ hàng");
        return response;
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ApiResponse<CartDTO> deleteCartItemSession(@PathVariable Long cartItemId, @RequestParam String sessionId) {
        ApiResponse<CartDTO> response = new ApiResponse<>();
        cartService.deleteCartItemSession(cartItemId, sessionId);
        response.setMessage("Đã xóa sản phẩm giỏ hàng");
        return response;
    }

    @DeleteMapping("/delete")
    public ApiResponse<CartDTO> deleteCartBySession(@RequestParam String sessionId) {
        ApiResponse<CartDTO> response = new ApiResponse<>();
        cartService.deleteSession(sessionId);
        response.setMessage("Đã xóa tất cả sản phẩm giỏ hàng");
        return response;
    }
}
