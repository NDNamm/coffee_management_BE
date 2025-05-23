package com.example.demo.dto;

import com.example.demo.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ImagesDTO {
    private Long id;
    private String imageUrl;
    private String publicId;
    private Long size;

}
