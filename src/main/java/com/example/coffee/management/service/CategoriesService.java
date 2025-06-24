package com.example.coffee.management.service;

import com.example.coffee.management.dto.CategoriesDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CategoriesService {
    Page<CategoriesDTO> getAllCategories(int page, int size);
    void addCategories(CategoriesDTO categoriesDTO, MultipartFile file);
    void updateCategories(CategoriesDTO categoriesDTO, Long id, MultipartFile file);
    void deleteCategories(Long id);
    Page<CategoriesDTO> selectCategoryByName(String name, int page, int size);

}
