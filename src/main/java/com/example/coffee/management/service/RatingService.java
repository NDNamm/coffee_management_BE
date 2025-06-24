package com.example.coffee.management.service;

import com.example.coffee.management.dto.RatingDTO;

import java.math.BigDecimal;
import java.util.List;

public interface RatingService {
    void createRating(RatingDTO ratingDTO, String email);
    List<RatingDTO> getAllRatings( Long productId);
    void updateRating(RatingDTO ratingDTO, Long productId, String email);
    void deleteRating(Long productId,String requesterId, String email);
    BigDecimal getAverageRating(Long productId);
}
