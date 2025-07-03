package com.example.coffee.management.controller;

import com.example.coffee.management.dto.ApiResponse;
import com.example.coffee.management.dto.CategoriesDTO;
import com.example.coffee.management.service.CategoriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoriesController {

    private static final Logger log = LoggerFactory.getLogger(CategoriesController.class);
    private final CategoriesService categoriesService;

    @GetMapping("")
    public ApiResponse<Page<CategoriesDTO>> getAllCategory(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size) {

        ApiResponse<Page<CategoriesDTO>> response = new ApiResponse<>();
        response.setData(categoriesService.getAllCategories(page, size));
        return response;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoriesDTO> addCategory(@RequestPart("categoryDTO") String categoriesJson,
                                                  @RequestPart("image") MultipartFile image) {
        ApiResponse<CategoriesDTO> response = new ApiResponse<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            CategoriesDTO categoriesDTO = mapper.readValue(categoriesJson, CategoriesDTO.class);

            categoriesService.addCategories(categoriesDTO, image);
            response.setData(categoriesDTO);
            response.setMessage("Thêm category thành công");
            return response;
        } catch (Exception e) {
            response.setCode(9999);
            response.setMessage("Thêm category thất bại: " + e.getMessage());
            return response;
        }
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CategoriesDTO> updateCategory(@PathVariable Long id,
                                                     @RequestPart("categoryDTO") String categoriesJson,
                                                     @RequestPart(value = "image", required = false) MultipartFile image) {
        ApiResponse<CategoriesDTO> response = new ApiResponse<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            CategoriesDTO categoriesDTO = mapper.readValue(categoriesJson, CategoriesDTO.class);

            categoriesService.updateCategories(categoriesDTO, id, image);
            response.setData(categoriesDTO);
            response.setMessage("Update category thành công");
            return response;
        } catch (Exception e) {
            response.setCode(9999);
            response.setMessage("Update category thất bại: " + e.getMessage());
            return response;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<CategoriesDTO> deleteCategory(@PathVariable Long id) {

        ApiResponse<CategoriesDTO> response = new ApiResponse<>();
        categoriesService.deleteCategories(id);
        response.setMessage("Delete category thành công");
        return response;
    }

    @GetMapping("/search/{name}")
    public ApiResponse<Page<CategoriesDTO>> selectCategory(@PathVariable String name,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size) {

        ApiResponse<Page<CategoriesDTO>> response = new ApiResponse<>();
        response.setData(categoriesService.selectCategoryByName(name, page, size));
        return response;
    }

}
