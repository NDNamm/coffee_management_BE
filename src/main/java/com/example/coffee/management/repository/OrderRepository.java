package com.example.coffee.management.repository;

import com.example.coffee.management.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where lower(o.address.receiverName) like lower(concat('%', :key , '%')) ")
    Page<Order> searchOrderByNameOrUserFullName(@Param("key") String key, Pageable pageable);


    @Query("SELECT DISTINCT o FROM Order o WHERE (:email IS NOT NULL AND o.user.email = :email) OR (:sessionId IS NOT NULL AND o.sessionId = :sessionId)")
    Page<Order> findOrdersByUserEmailOrSessionId(@Param("email") String email, @Param("sessionId") String sessionId, Pageable pageable);

    Page<Order> findOrdersByUserEmail(String email, Pageable pageable);
    Page<Order> findOrdersBySessionId(String sessionId, Pageable pageable);
}
