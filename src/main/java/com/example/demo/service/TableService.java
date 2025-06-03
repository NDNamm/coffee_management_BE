package com.example.demo.service;

import com.example.demo.dto.TableDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TableService {
    List<TableDTO> getAllTables();
    void addTable(TableDTO tableDTO);
    void updateTable(TableDTO tableDTO, Long id);
    void deleteTable(Long id);
}
