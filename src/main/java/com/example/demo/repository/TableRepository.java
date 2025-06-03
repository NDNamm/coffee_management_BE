package com.example.demo.repository;

import com.example.demo.model.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {
}
