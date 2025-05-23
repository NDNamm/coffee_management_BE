package com.example.demo.service.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.model.Images;
import com.example.demo.model.Product;
import com.example.demo.repository.ImagesRepository;
import com.example.demo.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImagesRepository imagesRepository;

    @Override
    @Transactional
    public String uploadCate(MultipartFile file) throws IOException {
        try {
            Map<String, Object> uploadCate = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "category"
            ));

            String coverUrl = uploadCate.get("secure_url").toString();
            return coverUrl;
        } catch (IOException e) {
            throw new RuntimeException("Loi up anh Cate: " + e.getMessage());
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
                Long size = (Long) results.get("size");
                String publicId = results.get("public_id").toString();

                Images images = new Images();
                images.setImageUrl(url);
                images.setPublicId(publicId);
                images.setSize(size);
                images.setProduct(product);
                imagesRepository.save(images);

                imageList.add(images);
            } catch (IOException e) {
                throw new RuntimeException("Loi up anh : " + e.getMessage());
            }
        }
        return imageList;
    }

}
