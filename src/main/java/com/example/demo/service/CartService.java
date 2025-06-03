package com.example.demo.service;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.model.Carts;
import com.example.demo.model.Users;

import java.util.List;

public interface CartService  {
    CartDTO getCartByUser(String email);
    void deleteCartByUser(String email);
    void addCart(String email, CartItemDTO CartItemDTO);
    void updateCart(String email, CartItemDTO CartItemDTO);
    void deleteCartItem(String email, Long cartItemId);

    CartDTO getCartBySession(String sessionId);
    void addSession(String sessionId, CartItemDTO CartItemDTO);
    void mergeSession(String sessionId, String email);
    void deleteSession(String sessionId);
    void updateSession(String sessionId, CartItemDTO CartItemDTO);
    void deleteCartItemSession(Long cartItemId, String sessionId);
}
