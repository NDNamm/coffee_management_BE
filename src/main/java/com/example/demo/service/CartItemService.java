package com.example.demo.service;

import com.example.demo.dto.CartItemDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CartItemService {
    Page<CartItemDTO> cartItems(int page, int size);
    void addCartItem(CartItemDTO cartItemDTO, Long userId);
    void updateCartItem(CartItemDTO cartItemDTO, Long cartId, Long userId);
    void deleteCartItem(Long userId);
    List<CartItemDTO> getCartItemsByUserId(Long userId);
}
