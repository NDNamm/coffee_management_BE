package com.example.demo.controller;

import com.example.demo.dto.CategoriesDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;
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
@RequestMapping("api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<Page<ProductDTO>> getAllProduct(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int size) {

        Page<ProductDTO> productDTO = productService.getAllProducts(page, size);
        return ResponseEntity.ok(productDTO);

    }

    @PostMapping(value = "/add/{cateId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@RequestPart("productDTO") String productJson,
                                        @RequestPart("image") MultipartFile[] image,
                                        @PathVariable Long cateId) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);

            productService.addProduct(productDTO, image, cateId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm san pham thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestPart("productDTO") String productJson,
                                           @RequestPart(value = "image", required = false) MultipartFile[] image) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);

            productService.updateProduct(productDTO, id, image);
            return ResponseEntity.status(HttpStatus.OK).body("Update San pham thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loi: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete san pham thanh cong");

    }

    @GetMapping("/search")
    ResponseEntity<Page<ProductDTO>> selectProduct(@RequestParam String name,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "6") int size) {

        Page<ProductDTO> productDTOS = productService.searchProductByName(name, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(productDTOS);

    }
    @GetMapping("/select/{cateId}")
    ResponseEntity<List<ProductDTO>> selectProductByCateId(@PathVariable Long cateId) {

        List<ProductDTO> productDTOS = productService.searchProductByCateId(cateId);
        return ResponseEntity.status(HttpStatus.OK).body(productDTOS);

    }

    @GetMapping("{productId}")
    ResponseEntity<ProductDTO> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.searchProductById(productId));
    }

}
