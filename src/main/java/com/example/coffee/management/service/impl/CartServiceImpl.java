package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.CartDTO;
import com.example.coffee.management.dto.CartItemDTO;
import com.example.coffee.management.dto.ProductDTO;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.CartItems;
import com.example.coffee.management.model.Carts;
import com.example.coffee.management.model.Product;
import com.example.coffee.management.model.Users;
import com.example.coffee.management.repository.CartItemRepository;
import com.example.coffee.management.repository.ProductRepository;
import com.example.coffee.management.service.CartService;
import com.example.coffee.management.repository.CartRepository;
import com.example.coffee.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDTO getCartByUser(String email) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Carts cart = getCartUser(user);
        CartDTO cartDTO = CartDTO.builder()
                .id(cart.getId())
                .userId(user.getId())
                .createdAt(cart.getCreatedAt())
                .build();
        return getCartDTO(cart, cartDTO);
    }

    @Transactional
    @Override
    public void deleteCartByUser(String email) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    private Carts getCartUser(Users user) {
        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart == null) {
            cart = new Carts();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    @Transactional
    public void addCart(String email, CartItemDTO cartItemDTO) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Carts cart = getCartUser(user);
        addProductToCart(cartItemDTO, cart);
    }

    @Override
    public void updateCartByUser(String email, CartItemDTO cartItemDTO) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Carts cart = cartRepository.findByUserWithItems(user);
        if (cart == null) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
        updateCart(cartItemDTO, cart);
    }

    @Override
    public void deleteCartItem(String email, Long cartItemId) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Carts cart = cartRepository.findByUserWithItems(user);
        deleteItems(cartItemId, cart);
    }

    //Cart cua Session
    private Carts getCartSession(String sessionId) {
        Carts cart = cartRepository.findBySessionIdWithItems(sessionId);
        if (cart == null) {
            cart = new Carts();
            cart.setSessionId(sessionId);
            cart.setCreatedAt(LocalDateTime.now());
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public CartDTO getCartBySession(String sessionId) {
        Carts cart = getCartSession(sessionId);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCreatedAt(cart.getCreatedAt());

        return getCartDTO(cart, cartDTO);
    }

    @Override
    @Transactional
    public void addSession(String sessionId, CartItemDTO cartItemDTO) {
        Carts cart = getCartSession(sessionId);
        addProductToCart(cartItemDTO, cart);
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
            cartRepository.save(cartSession);
        } else {
            throw new AppException(ErrorCode.SESSION_NOT_FOUND);
        }
    }

    @Override
    public void updateCartBySession(String sessionId, CartItemDTO cartItemDTO) {
        Carts cart = cartRepository.findBySessionIdWithItems(sessionId);
        if (cart != null) {
            updateCart(cartItemDTO, cart);
        } else {
            throw new AppException(ErrorCode.SESSION_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteCartItemSession(Long cartItemId, String sessionId) {
        Carts cart = cartRepository.findBySessionIdWithItems(sessionId);
        if (cart != null) {
            deleteItems(cartItemId, cart);
        } else {
            throw new AppException(ErrorCode.SESSION_NOT_FOUND);
        }
    }

    //Ham chung
    private CartDTO getCartDTO(Carts cart, CartDTO cartDTO) {
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();

        for (CartItems items : cart.getItems()) {
            Product product = items.getProduct();
            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .namePro(product.getNamePro())
                    .imageUrl(product.getImageUrl())
                    .price(product.getPrice())
                    .build();

            CartItemDTO cartItemDTO = CartItemDTO.builder()
                    .id(items.getId())
                    .productId(items.getProduct().getId())
                    .quantity(items.getQuantity())
                    .price(items.getPrice())
                    .totalPrice(items.getTotalPrice())
                    .product(productDTO)
                    .build();
            cartItemDTOS.add(cartItemDTO);
        }
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }

    private void addProductToCart(CartItemDTO cartItemDTO, Carts cart) {
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        CartItems cartItem;
        long addedQuantity = cartItemDTO.getQuantity();
        BigDecimal itemPrice = cartItemDTO.getPrice();

        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            long newQuantity = cartItem.getQuantity() + cartItemDTO.getQuantity();
            cartItem.setQuantity(newQuantity);
            cartItem.updateQuantityAndPrice(newQuantity, cartItem.getPrice());
        } else {
            cartItem = CartItems.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemDTO.getQuantity())
                    .price(cartItemDTO.getPrice())
                    .build();
            cartItem.updateQuantityAndPrice(addedQuantity, itemPrice);
        }

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    private void updateCart(CartItemDTO cartItemDTO, Carts cart) {
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<CartItems> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItems cartItem = existingItem.get();
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cartItem.setPrice(cartItemDTO.getPrice());
            cartItem.updateQuantityAndPrice(cartItemDTO.getQuantity(), cartItemDTO.getPrice());

            cartItemRepository.save(cartItem);
        } else {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        cartRepository.save(cart);
    }


    private void deleteItems(Long cartItemId, Carts cart) {
        if (cart == null) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
        CartItems cartItems = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cart.getItems().contains(cartItems)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_OPERATION);
        }
        cart.getItems().remove(cartItems);
        cartRepository.save(cart);
    }
}
