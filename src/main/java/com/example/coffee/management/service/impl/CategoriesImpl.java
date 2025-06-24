package com.example.coffee.management.service.impl;

import com.example.coffee.management.dto.CategoriesDTO;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.Categories;
import com.example.coffee.management.repository.CategoriesRepository;
import com.example.coffee.management.service.CategoriesService;
import com.example.coffee.management.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class CategoriesImpl implements CategoriesService {


    private final CategoriesRepository categoriesRepository;
    private final ImageService imageService;

    @Override
    public Page<CategoriesDTO> getAllCategories(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Categories> categoriesPage = categoriesRepository.findAll(pageable);

        return getCategoryDTOS(categoriesPage);
    }

    @Override
    public Page<CategoriesDTO> selectCategoryByName(String name, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Categories> categoriesPage = categoriesRepository.searchCategoriesByName(name, pageable);
        if (categoriesPage.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        return getCategoryDTOS(categoriesPage);
    }

    private Page<CategoriesDTO> getCategoryDTOS(Page<Categories> categories) {
        return categories.map(cate -> CategoriesDTO.builder()
                .id(cate.getId())
                .nameCate(cate.getNameCate())
                .imageUrl(cate.getImageUrl())
                .description(cate.getDescription())
                .createdAt(cate.getCreatedAt())
                .updatedAt(cate.getUpdatedAt())
                .build());
    }
    @Override
    public void addCategories(CategoriesDTO categoriesDTO, MultipartFile image) {

        if (categoriesRepository.findByNameCate(categoriesDTO.getNameCate()).isPresent())
            throw new AppException(ErrorCode.CATEGORY_NAME_EXISTED);

        try {
            Categories categories = Categories.builder()
                    .nameCate(categoriesDTO.getNameCate())
                    .imageUrl(imageService.uploadCate(image))
                    .description(categoriesDTO.getDescription())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            categoriesRepository.save(categories);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
    }

    @Override
    public void updateCategories(CategoriesDTO categoriesDTO, Long id, MultipartFile file) {
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoriesRepository.findByNameCate(categoriesDTO.getNameCate())
                .filter(cate -> !cate.getId().equals(id))
                .ifPresent(cate -> {
                    throw new AppException(ErrorCode.CATEGORY_NAME_EXISTED);
                });

        try {
            if (file != null && !file.isEmpty()) {
                String url = imageService.uploadCate(file);
                categories.setImageUrl(url);
            }
            categories.setNameCate(categoriesDTO.getNameCate());
            categories.setDescription(categoriesDTO.getDescription());
            categories.setUpdatedAt(LocalDateTime.now());
            categoriesRepository.save(categories);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
    }

    @Override
    public void deleteCategories(Long id) {
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoriesRepository.delete(categories);
    }



}
