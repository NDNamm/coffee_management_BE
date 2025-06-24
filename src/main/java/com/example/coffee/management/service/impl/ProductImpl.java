package com.example.coffee.management.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.coffee.management.dto.ImagesDTO;
import com.example.coffee.management.dto.ProductDTO;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.Categories;
import com.example.coffee.management.model.enums.ProductStatus;
import com.example.coffee.management.model.Images;
import com.example.coffee.management.model.Product;
import com.example.coffee.management.repository.CategoriesRepository;
import com.example.coffee.management.repository.ImagesRepository;
import com.example.coffee.management.repository.ProductRepository;
import com.example.coffee.management.service.ImageService;
import com.example.coffee.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor()
public class ProductImpl implements ProductService {


    private final ProductRepository productRepository;
    private final CategoriesRepository categoriesRepository;
    private final ImageService imageService;
    private final Cloudinary cloudinary;
    private final ImagesRepository imagesRepository;

    @Override
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Product> products = productRepository.findAll(pageable);

        return getProductDTOS(products);
    }

    @Override
    public Page<ProductDTO> searchProductByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Product> products = productRepository.searchProductByName(name, pageable);

        return getProductDTOS(products);
    }

    private Page<ProductDTO> getProductDTOS(Page<Product> products) {
        return products.map(product -> {
                    List<Images> images = product.getImages();
                    List<ImagesDTO> imageDTOs = null;
                    String imageUrl = null;
                    if (images != null && !images.isEmpty()) {
                        imageUrl = images.get(0).getImageUrl();

                        imageDTOs = images.stream()
                                .map(img -> ImagesDTO.builder()
                                        .id(img.getId())
                                        .publicId(img.getPublicId())
                                        .imageUrl(img.getImageUrl())
                                        .size(img.getSize())
                                        .build())
                                .toList();

                    }
                    return ProductDTO.builder()
                            .id(product.getId())
                            .namePro(product.getNamePro())
                            .price(product.getPrice())
                            .description(product.getDescription())
                            .status(product.getStatus())
                            .averageRating(product.getAverageRating())
                            .createdAt(product.getCreatedAt())
                            .updateAt(product.getUpdateAt())
                            .imageUrl(imageUrl)
                            .imagesDTO(imageDTOs)
                            .categoryId(product.getCategory().getId())
                            .build();
                }
        );
    }

    @Override
    public void addProduct(ProductDTO productDTO, MultipartFile[] image, Long categoryId) {

        Categories categories = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (productRepository.findProductByNamePro(productDTO.getNamePro()).isPresent())
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        try {
            Product product = Product.builder()
                    .namePro(productDTO.getNamePro())
                    .description(productDTO.getDescription())
                    .price(productDTO.getPrice())
                    .status(ProductStatus.AVAILABLE)
                    .createdAt(LocalDateTime.now())
                    .updateAt(LocalDateTime.now())
                    .category(categories)
                    .build();
            productRepository.save(product);

            List<Images> images = imageService.uploadProduct(image, productDTO.getNamePro(), product);
            String imageUrl = images.get(0).getImageUrl();

            product.setImageUrl(imageUrl);
            product.setImages(images);
            productRepository.save(product);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }

    }

    @Override
    public void updateProduct(ProductDTO productDTO, Long id, MultipartFile[] files) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.findProductByNamePro(productDTO.getNamePro())
                .filter(pro -> !pro.getId().equals(id))
                .ifPresent(pro -> {
                    throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
                });

        try {
            // Cập nhật thông tin sản phẩm
            product.setNamePro(productDTO.getNamePro());
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setUpdateAt(LocalDateTime.now());
            // Nếu status không null hoặc rỗng thì mới cập nhật
            if (productDTO.getStatus() != null) {
                product.setStatus(productDTO.getStatus());
            }
            // Nếu có ảnh mới thì mới xóa ảnh cũ và upload ảnh mới
            if (files != null && files.length > 0) {
                deleteOldImages(product);
                // Upload ảnh mới
                List<Images> newImages = imageService.uploadProduct(files, productDTO.getNamePro(), product);
                product.setImages(newImages);
                product.setImageUrl(newImages.get(0).getImageUrl());
            }
            productRepository.save(product);

        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        try {
            deleteOldImages(product);
            productRepository.delete(product);
        } catch (IOException e) {
            throw new AppException(ErrorCode.DELETE_IMAGE_FAIL);
        }
    }

    private void deleteOldImages(Product product) throws IOException {
        List<Images> oldImages = product.getImages();
        if (oldImages != null && !oldImages.isEmpty()) {
            for (Images image : oldImages) {
                cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
                imagesRepository.delete(image);
            }
        }
    }


    @Override
    public Page<ProductDTO> searchProductByCateId(Long cateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Product> productPage = productRepository.findProductsByCategoryId(cateId, pageable);

        return getProductDTOS(productPage);
    }

    @Override
    public ProductDTO searchProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Images> images = product.getImages();
        List<ImagesDTO> listImagesDTO = null;
        String imageUrl = null;
        if (images != null && !images.isEmpty()) {
            // Ảnh đại diện là ảnh đầu tiên
            imageUrl = images.get(0).getImageUrl();

            // Danh sách ảnh DTO
            listImagesDTO = images.stream()
                    .map(img -> {
                        ImagesDTO imgDto = new ImagesDTO();
                        imgDto.setId(img.getId());
                        imgDto.setPublicId(img.getPublicId());
                        imgDto.setImageUrl(img.getImageUrl());
                        return imgDto;
                    })
                    .toList();

        }
        return ProductDTO.builder()
                .id(product.getId())
                .namePro(product.getNamePro())
                .price(product.getPrice())
                .description(product.getDescription())
                .status(product.getStatus())
                .averageRating(product.getAverageRating())
                .createdAt(product.getCreatedAt())
                .updateAt(product.getUpdateAt())
                .imageUrl(imageUrl)
                .imagesDTO(listImagesDTO)
                .categoryId(product.getCategory().getId())
                .build();

    }
}
