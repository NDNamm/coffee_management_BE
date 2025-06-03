package com.example.demo.repository;

import com.example.demo.model.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    Optional<Categories> findByName(String name);

    @Query("select c from Categories c where lower(c.name) like lower(concat('%', :nameCate, '%') ) ")
    Page<Categories> searchCategoriesByName(String nameCate, Pageable pageable);

    @Query("select c from Categories c where lower(c.name) = lower(:nameCate)")
    List<Categories> findCategoriesByName(String nameCate);

}
