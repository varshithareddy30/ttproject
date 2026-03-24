package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Domain;
import com.example.demo.repository.DomainRepository;

@Service
public class DomainService {

    @Autowired
    DomainRepository repo;

    public Domain selectDomain(Domain domain){
        return repo.save(domain);
    }

    public List<Domain> getStudentDomains(Long studentId){
        return repo.findByStudentId(studentId);
    }
}