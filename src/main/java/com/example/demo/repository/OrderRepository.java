package com.example.demo.repository;

import com.example.demo.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where lower(o.name) like lower(concat('%', :key , '%')) ")
    Page<Order> searchOrderByNameOrUserFullName(@Param("key") String key, Pageable pageable);


    @Query("SELECT DISTINCT o FROM Order o WHERE (:email IS NOT NULL AND o.user.email = :email) OR (:sessionId IS NOT NULL AND o.sessionId = :sessionId)")
    List<Order> findOrdersByUserEmailOrSessionId(@Param("email") String email, @Param("sessionId") String sessionId);

    List<Order> findOrdersByUserEmail(String email);
    List<Order> findOrdersBySessionId(String sessionId);
}
