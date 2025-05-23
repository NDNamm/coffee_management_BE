package com.example.demo.controller;

import com.example.demo.dto.CategoriesDTO;
import com.example.demo.model.Categories;
import com.example.demo.service.CategoriesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("")
    ResponseEntity<Page<CategoriesDTO>> getAllCategory(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size) {

        Page<CategoriesDTO> CategoryDTO = categoriesService.getAllCategories(page, size);
        return ResponseEntity.ok(CategoryDTO);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity addCategory(@RequestPart("categoryDTO") String categoriesJson,
                                      @RequestPart("image") MultipartFile image) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            CategoriesDTO categoriesDTO = mapper.readValue(categoriesJson, CategoriesDTO.class);

            categoriesService.addCategories(categoriesDTO, image);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm danh muc thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory(@PathVariable Long id,
                                            @RequestPart("categoryDTO") String categoriesJson,
                                            @RequestPart("image") MultipartFile image) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CategoriesDTO categoriesDTO = mapper.readValue(categoriesJson, CategoriesDTO.class);

            categoriesService.updateCategories(categoriesDTO, id, image);
            return ResponseEntity.status(HttpStatus.OK).body("Update danh muc thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loi: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id) {

        categoriesService.deleteCategories(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete danh muc thanh cong");

    }

    @GetMapping("/select/{name}")
    ResponseEntity<List<CategoriesDTO>> selectCategory(@PathVariable String name) {

        List<CategoriesDTO> categoriesDTOS = categoriesService.selectCategoryByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(categoriesDTOS);

    }

}
