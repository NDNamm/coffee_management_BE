package com.example.coffee.management.controller;

import com.example.coffee.management.dto.RatingDTO;
import com.example.coffee.management.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("{productId}")
    public ResponseEntity<List<RatingDTO>> getAllRating(@PathVariable Long productId
    ) {
        return ResponseEntity.ok(ratingService.getAllRatings(productId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRating(@RequestBody RatingDTO ratingDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ratingService.createRating(ratingDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Danh gia san pham thanh cong");
    }

    @PutMapping("update/{productId}")
    public ResponseEntity<?> updateRating(@PathVariable Long productId,
                                          @RequestBody RatingDTO ratingDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ratingService.updateRating(ratingDTO, productId, email);
        return ResponseEntity.ok("Sua danh gia san pham thanh cong");
    }

    @DeleteMapping("delete/{productId}/{requestID}")
    public ResponseEntity<?> deleteRating(
            @PathVariable Long productId,
            @PathVariable String requestID) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ratingService.deleteRating(productId, requestID, email);
        return ResponseEntity.ok("Xoa danh gia san pham thanh cong");
    }

    @GetMapping("average/{productId}")
    public ResponseEntity<BigDecimal> getAverageRating(@PathVariable Long productId) {
        return ResponseEntity.ok(ratingService.getAverageRating(productId));
    }

}
