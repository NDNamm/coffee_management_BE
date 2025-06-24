package com.example.coffee.management.repository;

import com.example.coffee.management.model.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findByNameCate(String nameCate);

    @Query("select c from Categories c where lower(c.nameCate) like lower(concat('%', :nameCate, '%') ) ")
    Page<Categories> searchCategoriesByName(String nameCate, Pageable pageable);

    @Query("select c from Categories c where lower(c.nameCate) = lower(:nameCate)")
    List<Categories> findCategoriesByName(String nameCate);

}
