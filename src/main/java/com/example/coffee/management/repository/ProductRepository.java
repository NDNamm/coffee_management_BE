package com.example.coffee.management.repository;

import com.example.coffee.management.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where lower(p.namePro) like lower(concat('%', :namePro, '%') ) ")
    Page<Product> searchProductByName(String namePro, Pageable pageable);

//    @Query("select p from Product p where lower(p.namePro) = lower(:namePro)")
//    List<Product> findProductByName(String namePro);

    Optional<Product> findProductByNamePro(String namePro);

    Page<Product> findProductsByCategoryId(Long categoryId, Pageable pageable);
}
