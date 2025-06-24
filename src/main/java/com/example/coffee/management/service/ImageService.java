package com.example.coffee.management.service;

import com.example.coffee.management.model.Images;
import com.example.coffee.management.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    public String uploadCate(MultipartFile file) throws IOException;
    public List<Images> uploadProduct(MultipartFile[] file, String name, Product product) throws IOException;
}
