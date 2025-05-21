package com.example.demo.dto;

import com.example.demo.model.Enum.TableStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableDTO {
    private Long id;
    private String name;
    private TableStatus status;
}
