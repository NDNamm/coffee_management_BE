package com.example.demo.dto;

import com.example.demo.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoriesDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private List<Product> product;

}
