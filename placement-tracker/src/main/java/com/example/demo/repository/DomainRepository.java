package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    List<Domain> findByStudentId(Long studentId);
    void deleteByStudentId(Long studentId);

}