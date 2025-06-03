package com.example.demo.controller;

import com.example.demo.dto.TableDTO;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/table")
@RestController
public class TableController {
    @Autowired
    private TableService tableService;

    @GetMapping("")
    ResponseEntity<List<TableDTO>> getTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @PostMapping("/add")
    ResponseEntity<?> addTable(@RequestBody TableDTO tableDTO) {
        tableService.addTable(tableDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tao ban thanh cong");
    }

    @PutMapping("update/{id}")
    ResponseEntity<?> updateTable(@PathVariable Long id, @RequestBody TableDTO tableDTO) {
        tableService.updateTable(tableDTO, id);
        return ResponseEntity.ok("Update ban thanh cong");
    }

    @DeleteMapping("delete/{id}")
    ResponseEntity<?> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.ok("Delete ban thanh cong");
    }
}
