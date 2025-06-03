package com.example.demo.repository;

import com.example.demo.model.Categories;
import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query("select p from Product p where lower(p.name) like lower(concat('%', :namePro, '%') ) ")
    Page<Product> searchProductByName(String namePro, Pageable pageable);

    @Query("select p from Product p where lower(p.name) = lower(:namePro)")
    List<Product> findProductByName(String namePro);

    @Query(value = "SELECT * FROM products ORDER BY RAND()", nativeQuery = true)
    Page<Product> findAllRandom(Pageable pageable);

    List<Product> findProductsByCategoryId(Long categoryId);
}
