package com.example.coffee.management.repository;

import com.example.coffee.management.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Page<Rating> findRatingByProductId(Long productId, Pageable pageable);

    Optional<Rating> findByProductIdAndUserId(Long productId, Long userId);
    List<Rating> findByProductId(Long productId);
}
