package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.model.CartItems;
import com.example.demo.model.Carts;
import com.example.demo.model.Product;
import com.example.demo.model.Users;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDTO getCartByUser(String email) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart == null) {
            cart = new Carts();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCreatedAt(cart.getCreatedAt());
        cartDTO.setUser(user);


        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for (CartItems items : cart.getItems()) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(items.getId());
            cartItemDTO.setProduct(items.getProduct());
            cartItemDTO.setQuantity(items.getQuantity());
            cartItemDTO.setPrice(items.getPrice());
            cartItemDTO.setTotalPrice(items.getTotalPrice());
            cartItemDTOS.add(cartItemDTO);
        }
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }

    @Transactional
    @Override
    public void deleteCartByUser(String email) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart != null) {
            cart.getItems().clear();

            cartRepository.delete(cart);
        }
    }

    @Override
    @Transactional
    public void addCart(String email, CartItemDTO cartItemDTO) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart == null) {
            cart = new Carts();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart = cartRepository.save(cart);
        }

        Product product = productRepository.findById(cartItemDTO.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems cartItems = existingItem.get();
            cartItems.setQuantity(cartItemDTO.getQuantity() + cartItems.getQuantity());
            cartItems.setTotalPrice(cartItemDTO.getTotalPrice());
            cartItemRepository.save(cartItems);
        } else {
            CartItems cartItems = new CartItems();
            cartItems.setCart(cart);
            cartItems.setProduct(product);
            cartItems.setQuantity(cartItemDTO.getQuantity());
            cartItems.setPrice(cartItemDTO.getPrice());
            cartItems.setTotalPrice(cartItemDTO.getTotalPrice());
            cartItemRepository.save(cartItems);
        }
        cartRepository.save(cart);
    }

    @Override
    public void updateCart(String email, CartItemDTO cartItemDTO) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        Product product = productRepository.findById(cartItemDTO.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItem.setPrice(cartItemDTO.getPrice());
            cartItem.setTotalPrice(cartItemDTO.getTotalPrice());
        } else {
            throw new RuntimeException("Cart item not found in cart");
        }

        cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(String email, Long cartItemId) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CartItems cartItems = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(cartItems);
    }


    //Cart cua Session
    @Override
    public CartDTO getCartBySession(String sessionId) {
        Carts cart = cartRepository.findBySessionIdWithItems(sessionId);
        if (cart == null) {
            cart = new Carts();
            cart.setSessionId(sessionId);
            cart.setCreatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCreatedAt(cart.getCreatedAt());

        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for (CartItems items : cart.getItems()) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(items.getId());
            cartItemDTO.setProduct(items.getProduct());
            cartItemDTO.setQuantity(items.getQuantity());
            cartItemDTO.setPrice(items.getPrice());
            cartItemDTO.setTotalPrice(items.getTotalPrice());
            cartItemDTOS.add(cartItemDTO);
        }
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }

    @Override
    @Transactional
    public void addSession(String sessionId, CartItemDTO cartItemDTO) {
        Carts cart = cartRepository.findBySessionIdWithItems(sessionId);
        if (cart == null) {
            cart = new Carts();
            cart.setSessionId(sessionId);
            cart.setCreatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }

        Product product = productRepository.findById(cartItemDTO.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItem.setPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity())));
            cartItemRepository.save(cartItem);
        } else {
            CartItems cartItem = new CartItems();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItem.setPrice(cartItemDTO.getPrice());
            cartItem.setTotalPrice(cartItemDTO.getTotalPrice());
            cartItemRepository.save(cartItem);
        }
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void mergeSession(String sessionId, String email) {
        Carts cart = cartRepository.findBySessionIdWithItems(sessionId);
        if (cart == null || cart.getItems().isEmpty()) return;

        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Carts userCart = cartRepository.findByUserWithItems(user);
        if (userCart == null) {
            userCart = new Carts();
            userCart.setUser(user);
            userCart.setCreatedAt(LocalDateTime.now());
            cartRepository.save(userCart);
        }

        for (CartItems cartItem : cart.getItems()) {
            Optional<CartItems> existing = userCart.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(cartItem.getProduct().getId()))
                    .findFirst();

            if (existing.isPresent()) {
                CartItems existItem = existing.get();
                existItem.setQuantity(existItem.getQuantity() + cartItem.getQuantity());
                existItem.setTotalPrice(existItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                cartItemRepository.save(existItem);
            } else {
                CartItems cartItems = new CartItems();
                cartItems.setCart(userCart);
                cartItems.setProduct(cartItem.getProduct());
                cartItems.setQuantity(cartItem.getQuantity());
                cartItems.setPrice(cartItem.getPrice());
                cartItems.setTotalPrice(cartItem.getTotalPrice());
                cartItemRepository.save(cartItems);
            }

            cartRepository.save(userCart);
            cartItemRepository.deleteAll(cart.getItems());
            cartRepository.delete(cart);
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        Carts cartSession = cartRepository.findBySessionIdWithItems(sessionId);
        if (cartSession != null) {
            cartSession.getItems().clear();

            cartRepository.delete(cartSession);
        }
    }

    @Override
    public void updateSession(String sessionId, CartItemDTO cartItemDTO) {
        Carts carts = cartRepository.findBySessionIdWithItems(sessionId);
        if (carts != null) {
            Product product = productRepository.findById(cartItemDTO.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Optional<CartItems> existing = carts.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .findFirst();

            if (existing.isPresent()) {
                CartItems cartItem = existing.get();
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItem.setPrice(cartItem.getPrice());
                cartItem.setTotalPrice(cartItem.getTotalPrice());
            } else {
                throw new RuntimeException("Product not found");
            }
            cartRepository.save(carts);

        }
    }

    @Override
    @Transactional
    public void deleteCartItemSession( Long cartItemId,String sessionId) {

        Carts cartSession = cartRepository.findBySessionIdWithItems(sessionId);
        if (cartSession != null) {
            CartItems cartItems = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));

            cartSession.getItems().remove(cartItems);
            cartItemRepository.delete(cartItems);
        }

    }

}
