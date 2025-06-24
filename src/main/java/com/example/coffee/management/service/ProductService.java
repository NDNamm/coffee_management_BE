package com.example.coffee.management.service;

import com.example.coffee.management.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Page<ProductDTO> getAllProducts(int page, int size);
    void addProduct(ProductDTO productDTO, MultipartFile[] file, Long cateId);
    void updateProduct(ProductDTO productDTO, Long id, MultipartFile[] file);
    void deleteProduct(Long id);
    Page<ProductDTO> searchProductByName(String name, int page, int size);
    Page<ProductDTO> searchProductByCateId(Long cateId, int page, int size);
    ProductDTO searchProductById(Long id);
}
