package com.example.demo.service;

import com.example.demo.dto.CategoriesDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoriesService {
    Page<CategoriesDTO> getAllCategories(int page, int size);
    void addCategories(CategoriesDTO categoriesDTO, MultipartFile file);
    void updateCategories(CategoriesDTO categoriesDTO, Long id, MultipartFile file);
    void deleteCategories(Long id);
    List<CategoriesDTO> selectCategoryByName(String name);

}
