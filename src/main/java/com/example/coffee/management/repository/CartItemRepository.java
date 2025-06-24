package com.example.coffee.management.repository;

import com.example.coffee.management.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findByCartId(Long cartId);

}
