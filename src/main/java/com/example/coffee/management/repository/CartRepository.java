package com.example.coffee.management.repository;

import com.example.coffee.management.model.Carts;
import com.example.coffee.management.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Carts, Long> {
    Carts findByUser(Users user);
    @Query("SELECT c FROM Carts c LEFT JOIN FETCH c.items WHERE c.user = :user")
    Carts findByUserWithItems(@Param("user") Users user);

    @Query("SELECT c FROM Carts c LEFT JOIN FETCH c.items WHERE c.sessionId = :sessionId")
    Carts findBySessionIdWithItems(@Param("sessionId") String sessionId);

    Optional<Carts> findBySessionId(String sessionId);

}

