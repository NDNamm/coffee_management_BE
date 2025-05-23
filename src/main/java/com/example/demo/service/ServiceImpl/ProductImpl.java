package com.example.demo.service.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dto.ImagesDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.model.Categories;
import com.example.demo.model.Enum.ProductStatus;
import com.example.demo.model.Images;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoriesRepository;
import com.example.demo.repository.ImagesRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ImageService;
import com.example.demo.service.ProductService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImagesRepository imagesRepository;

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Product> products = productRepository.findAllRandom(pageable);

        return products.map(product -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setDescription(product.getDescription());
            productDTO.setStatus(product.getStatus());
            productDTO.setCreatedAt(product.getCreatedAt());
            productDTO.setUpdateAt(product.getUpdateAt());

            List<Images> images = product.getImages();
            if (images != null && !images.isEmpty()) {
                // Ảnh đại diện là ảnh đầu tiên
                productDTO.setImageUrl(images.get(0).getImageUrl());

                // Danh sách ảnh DTO
                List<ImagesDTO> imageDTOs = images.stream()
                        .map(img -> {
                            ImagesDTO imgDto = new ImagesDTO();
                            imgDto.setId(img.getId());
                            imgDto.setPublicId(img.getPublicId());
                            imgDto.setImageUrl(img.getImageUrl());
                            return imgDto;
                        })
                        .collect(Collectors.toList());
                productDTO.setImagesDTO(imageDTOs);

            }
            productDTO.setCategoryId(product.getCategory().getId());
            return productDTO;
        });
    }

    @Override
    public void addProduct(ProductDTO productDTO, MultipartFile[] file, Long categoryId) {

        Categories categories = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh muc k ton tai"));

        try {
            List<Product> products = productRepository.findProductByName(productDTO.getName());
            if (products.isEmpty()) {
                Product product = new Product();
                product.setName(productDTO.getName());
                product.setPrice(productDTO.getPrice());
                product.setDescription(productDTO.getDescription());
                product.setStatus(ProductStatus.AVAILABLE);
                product.setCategory(categories);
                product.setCreatedAt(LocalDateTime.now());
                product.setUpdateAt(LocalDateTime.now());
                productRepository.save(product);
                List<Images> imageUrls = imageService.uploadProduct(file, productDTO.getName(), product);
                String imageCover = imageUrls.get(0).getImageUrl();
                product.setImageUrl(imageCover);
                productRepository.save(product);
            } else {
                throw new RuntimeException("San pham muc da ton tai");
            }

        } catch (IOException e) {
            throw new RuntimeException("Loi up anh:" + e.getMessage());
        }

    }

    @Override
    public void updateProduct(ProductDTO productDTO, Long id, MultipartFile[] file) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("San pham k ton tai"));
        try {

            List<Images> oldImages = product.getImages();
            if (!oldImages.isEmpty() && oldImages != null) {
                for (Images image : oldImages) {
                    //Xoa anh cu
                    cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
                    //Xoa o DB
                    imagesRepository.delete(image);
                }
            }
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setStatus(productDTO.getStatus());
            List<Images> imageUrls = imageService.uploadProduct(file, productDTO.getName(), product);
            String imageCover = imageUrls.get(0).getImageUrl();
            product.setImageUrl(imageCover);
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdateAt(LocalDateTime.now());
            productRepository.save(product);

        } catch (IOException e) {
            throw new RuntimeException("Loi up anh:" + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("San pham k ton tai"));

        List<Images> oldImages = product.getImages();
        try {
            if (!oldImages.isEmpty() && oldImages != null) {
                for (Images image : oldImages) {
                    //Xoa anh cu
                    cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
                    //Xoa o DB
                    imagesRepository.delete(image);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Loi xoa anh:" + e.getMessage());
        }
        productRepository.delete(product);

    }

    @Override
    public List<ProductDTO> selectProductByName(String name) {

        List<Product> products = productRepository.searchProductByName(name);
        return products.stream().map(
                product -> {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId(product.getId());
                    productDTO.setName(product.getName());
                    productDTO.setPrice(product.getPrice());
                    productDTO.setDescription(product.getDescription());
                    List<Images> images = product.getImages();
                    if (images != null && !images.isEmpty()) {
                        productDTO.setImageUrl(images.get(0).getImageUrl());
                        List<ImagesDTO> imageDTOs = images.stream()
                                .map(img -> {
                                    ImagesDTO imgDto = new ImagesDTO();
                                    imgDto.setId(img.getId());
                                    imgDto.setPublicId(img.getPublicId());
                                    imgDto.setImageUrl(img.getImageUrl());
                                    return imgDto;
                                })
                                .collect(Collectors.toList());
                        productDTO.setImagesDTO(imageDTOs);

                    }
                    productDTO.setCreatedAt(product.getCreatedAt());
                    productDTO.setUpdateAt(product.getUpdateAt());
                    productDTO.setCategoryId(product.getCategory().getId());
                    return productDTO;
                }
        ).collect(Collectors.toList());
    }
}
