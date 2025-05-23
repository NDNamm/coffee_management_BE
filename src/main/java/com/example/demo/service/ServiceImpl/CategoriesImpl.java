package com.example.demo.service.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.example.demo.dto.CategoriesDTO;
import com.example.demo.model.Categories;
import com.example.demo.repository.CategoriesRepository;
import com.example.demo.service.CategoriesService;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImageService imageService;

    @Override
    public Page<CategoriesDTO> getAllCategories(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Categories> categories = categoriesRepository.findAll(pageable);

        return categories.map(categories1 -> {
            CategoriesDTO categoriesDTO = new CategoriesDTO();
            categoriesDTO.setId(categories1.getId());
            categoriesDTO.setName(categories1.getName());
            categoriesDTO.setImageUrl(categories1.getImageUrl());
            categoriesDTO.setDescription(categories1.getDescription());
            categoriesDTO.setCreatedAt(categories1.getCreatedAt());
            categoriesDTO.setUpdatedAt(categories1.getUpdatedAt());
            return categoriesDTO;
        });
    }

    @Override
    public void addCategories(CategoriesDTO categoriesDTO, MultipartFile file) {
        List<Categories> categories = categoriesRepository.findCategoriesByName(categoriesDTO.getName());
        try {
            if (categories.isEmpty()) {
                Categories categories1 = new Categories();
                categories1.setName(categoriesDTO.getName());
                categories1.setDescription(categoriesDTO.getDescription());
                categories1.setCreatedAt(LocalDateTime.now());
                categories1.setUpdatedAt(LocalDateTime.now());
                String url = imageService.uploadCate(file);
                categories1.setImageUrl(url);
                categoriesRepository.save(categories1);
            }
            else {
                throw new RuntimeException("Danh muc da ton tai");
            }
        }catch (IOException e){
            throw new RuntimeException("Loi up anh:" + e.getMessage());
        }
    }

    @Override
    public void updateCategories(CategoriesDTO categoriesDTO, Long id, MultipartFile file) {
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh muc k ton tai"));
        try {
            categories.setName(categoriesDTO.getName());
            categories.setDescription(categoriesDTO.getDescription());
            String url = imageService.uploadCate(file);
            categories.setImageUrl(url);
            categories.setDescription(categoriesDTO.getDescription());
            categories.setUpdatedAt(LocalDateTime.now());
            categoriesRepository.save(categories);
        }
        catch (IOException e){
            throw new RuntimeException("Loi up anh:" + e.getMessage());
        }
    }

    @Override
    public void deleteCategories(Long id) {
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh muc k ton tai"));

        categoriesRepository.delete(categories);
    }

    @Override
    public List<CategoriesDTO> selectCategoryByName(String name) {
        List<Categories> categories = categoriesRepository.searchCategoriesByName(name);

        if (categories.isEmpty()) {
            throw new RuntimeException("Danh muc k ton tai");
        }
        return categories.stream().map(
                categories1 -> {
                    CategoriesDTO categoriesDTO = new CategoriesDTO();
                    categoriesDTO.setId(categories1.getId());
                    categoriesDTO.setName(categories1.getName());
                    categoriesDTO.setDescription(categories1.getDescription());
                    categoriesDTO.setImageUrl(categories1.getImageUrl());
                    categoriesDTO.setCreatedAt(categories1.getCreatedAt());
                    categoriesDTO.setUpdatedAt(categories1.getUpdatedAt());
                    return categoriesDTO;
                }
        ).collect(Collectors.toList());
    }

}
