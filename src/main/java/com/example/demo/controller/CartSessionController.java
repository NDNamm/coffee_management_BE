package com.example.demo.controller;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.model.Carts;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/cart/session")
public class CartSessionController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createSessionCart() {
        String sessionId = UUID.randomUUID().toString();

        Carts carts = new Carts();
        carts.setSessionId(sessionId);
        carts.setCreatedAt(LocalDateTime.now());
        cartRepository.save(carts);

        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        return ResponseEntity.ok(response);
    }

    //Gio hang Ao
    @GetMapping("")
    public ResponseEntity<CartDTO> getCartNySession(@RequestParam String sessionId) {
        CartDTO cartDTO = cartService.getCartBySession(sessionId);
        return ResponseEntity.ok(cartDTO);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addCartSession(@RequestBody CartItemDTO cartItemDTO, @RequestParam String sessionId) {
        cartService.addSession(sessionId, cartItemDTO);
        return ResponseEntity.ok("Da Them gio hang");
    }

    //Gop gio hang vao khi dang nhap
    @PostMapping("/merge")
    public ResponseEntity<?> mergeCartSession(@RequestParam String sessionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.mergeSession(sessionId, email);
        return ResponseEntity.ok("Da gop gio hang vao gio hang nguoi dung");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCartSession(@RequestBody CartItemDTO cartItemDTO, @RequestParam String sessionId) {
        cartService.updateSession(sessionId, cartItemDTO);
        return ResponseEntity.ok("Da Sua gio hang");
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<?> deleteCartItemSession(@PathVariable Long cartItemId, @RequestParam String sessionId) {
        cartService.deleteCartItemSession(cartItemId,sessionId);
        return ResponseEntity.ok("Da Xoa sp trong gio hang");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCartBySession( @RequestParam String sessionId) {
        cartService.deleteSession(sessionId);
        return ResponseEntity.ok("Da Xoa sp trong gio hang");
    }
}
