package com.example.demo.service.ServiceImpl;

import com.example.demo.dto.TableDTO;
import com.example.demo.model.Enum.TableStatus;
import com.example.demo.model.Tables;
import com.example.demo.repository.TableRepository;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableImpl implements TableService {

    @Autowired
    private TableRepository tableRepository;

    @Override
    public List<TableDTO> getAllTables() {

        List<Tables> table = tableRepository.findAll();

        return table.stream().map(tables -> {
            TableDTO tableDTO = new TableDTO();
            tableDTO.setId(tables.getId());
            tableDTO.setName(tables.getName());
            tableDTO.setStatus(tables.getStatus());
            return tableDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void addTable(TableDTO tableDTO) {

        Tables table = new Tables();
        table.setName(tableDTO.getName());
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
    }

    @Override
    public void updateTable(TableDTO tableDTO, Long id) {
        Tables tables = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ban k ton tai"));

        tables.setName(tableDTO.getName());
        tables.setStatus(tableDTO.getStatus());
        tableRepository.save(tables);
    }

    @Override
    public void deleteTable(Long id) {
        Tables tables = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ban k ton tai"));

        tableRepository.delete(tables);
    }
}
