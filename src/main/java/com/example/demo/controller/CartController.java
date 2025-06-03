package com.example.demo.controller;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.model.Carts;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("")
    public ResponseEntity<CartDTO> getCartOfCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CartDTO cartDTO = cartService.getCartByUser(email);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody CartItemDTO cartItemDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.addCart(email, cartItemDTO);
        return ResponseEntity.ok("Đã thêm vào giỏ hàng");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody CartItemDTO cartItemDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.updateCart(email, cartItemDTO);
        return ResponseEntity.ok("Đã sua giỏ hàng");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.deleteCartByUser(email);
        return ResponseEntity.ok("Da xoa gio hang");
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.deleteCartItem(email, cartItemId);
        return ResponseEntity.ok("Da xoa gio hang");
    }


}
