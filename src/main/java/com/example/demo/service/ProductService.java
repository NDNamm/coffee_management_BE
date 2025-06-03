package com.example.demo.service;

import com.example.demo.dto.CategoriesDTO;
import com.example.demo.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Page<ProductDTO> getAllProducts(int page, int size);
    void addProduct(ProductDTO productDTO, MultipartFile[] file, Long cateId);
    void updateProduct(ProductDTO productDTO, Long id, MultipartFile[] file);
    void deleteProduct(Long id);
    Page<ProductDTO> searchProductByName(String name, int page, int size);
    List<ProductDTO> searchProductByCateId(Long cateId);
    ProductDTO searchProductById(Long id);
}
