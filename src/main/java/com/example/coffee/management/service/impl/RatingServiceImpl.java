package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.RatingDTO;
import com.example.coffee.management.model.Product;
import com.example.coffee.management.model.Rating;
import com.example.coffee.management.model.Users;
import com.example.coffee.management.repository.ProductRepository;
import com.example.coffee.management.repository.RatingRepository;
import com.example.coffee.management.repository.RoleRepository;
import com.example.coffee.management.repository.UserRepository;
import com.example.coffee.management.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void createRating(RatingDTO ratingDTO, String email) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (user.getRole().getRoleName().equals("STAFF")) {
            throw new RuntimeException("Nhan vien k co quyen danh gia");
        }

        Product product = productRepository.findById(ratingDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setProduct(product);
        rating.setRatingValue(ratingDTO.getRatingValue());
        rating.setComment(ratingDTO.getComment());
        rating.setCreatedAt(LocalDateTime.now());

        ratingRepository.save(rating);
    }

    @Override
    public List<RatingDTO> getAllRatings( Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        List<Rating>ratings = ratingRepository.findByProductId(productId);

        return ratings.stream().map(rating -> {
            RatingDTO ratingDTO = new RatingDTO();
            ratingDTO.setUserName(rating.getUser().getFullName());
            ratingDTO.setRatingValue(rating.getRatingValue());
            ratingDTO.setComment(rating.getComment());
            ratingDTO.setCreatedAt(rating.getCreatedAt());
            ratingDTO.setProduct(product);
            return ratingDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateRating(RatingDTO ratingDTO, Long productId, String email) {
        Users user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (user.getRole().getRoleName().equals("STAFF")) {
            throw new RuntimeException("Nhan vien k co quyen danh gia");
        }

        Product product = productRepository.findById(ratingDTO.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        Rating rating = ratingRepository.findByProductIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá để cập nhật"));

        rating.setRatingValue(ratingDTO.getRatingValue());
        rating.setComment(ratingDTO.getComment());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);
    }

    @Override
    public void deleteRating(Long productId, String email, String requesterId) {
        Users request = userRepository.findUserByEmail(requesterId)
                .orElseThrow(() -> new RuntimeException("Requester Not Found"));

        Users users = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Rating rating = ratingRepository.findByProductIdAndUserId(productId, users.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá để cập nhật"));

        if (request.getId().equals(users.getId()) || request.getRole().getRoleName().equals("ADMIN")) {
            ratingRepository.delete(rating);
        }

        throw new RuntimeException("Ban k co quyen xoa danh gia nay");
    }

    @Override
    public BigDecimal getAverageRating(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        List<Rating> ratings = ratingRepository.findByProductId(productId);
        if (ratings.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (Rating rating : ratings) {
            sum = sum.add(BigDecimal.valueOf(rating.getRatingValue()));
        }

        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);
        product.setAverageRating(average);
        productRepository.save(product);

        return average;
    }
}
