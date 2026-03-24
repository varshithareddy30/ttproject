package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Student;
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public Student register(@RequestBody Student student){
        return authService.register(student);
    }

    @PostMapping("/login")
    public Student login(@RequestBody Student student){
        return authService.login(student.getEmail(), student.getPassword());
    }

}