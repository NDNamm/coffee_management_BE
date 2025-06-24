package com.example.coffee.management.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.coffee.management.exception.AppException;
import com.example.coffee.management.exception.ErrorCode;
import com.example.coffee.management.model.Images;
import com.example.coffee.management.model.Product;
import com.example.coffee.management.repository.ImagesRepository;
import com.example.coffee.management.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;
    private final ImagesRepository imagesRepository;

    @Override
    @Transactional
    public String uploadCate(MultipartFile file) throws IOException {
        try {
            Map<?,?> uploadCate = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "category"
            ));
            return uploadCate.get("secure_url").toString();
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
    }

    @Override
    @Transactional
    public List<Images> uploadProduct(MultipartFile[] file, String name, Product product) throws IOException {
        List<Images> imageList = new ArrayList<>();
        for (MultipartFile fileItem : file) {
            try {
                Map<?, ?> results = cloudinary.uploader().upload(fileItem.getBytes(), ObjectUtils.asMap(
                        "folder", name
                ));

                String url = results.get("secure_url").toString();
                String publicId = results.get("public_id").toString();

                Images images = new Images();
                images.setImageUrl(url);
                images.setPublicId(publicId);
                images.setSize(fileItem.getSize());
                images.setProduct(product);
                imagesRepository.save(images);

                imageList.add(images);
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
            }
        }
        return imageList;
    }

}
