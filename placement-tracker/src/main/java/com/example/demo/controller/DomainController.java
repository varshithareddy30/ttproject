package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Domain;
import com.example.demo.service.DomainService;

@RestController
@RequestMapping("/domain")
@CrossOrigin(origins="http://localhost:3000")
public class DomainController {

    @Autowired
    DomainService service;

    @PostMapping("/select")
    public Domain selectDomain(@RequestBody Domain domain){
        return service.selectDomain(domain);
    }

    @GetMapping("/student/{id}")
    public List<Domain> getStudentDomains(@PathVariable Long id){
        return service.getStudentDomains(id);
    }
}