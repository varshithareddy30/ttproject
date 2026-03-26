package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Student;
import com.example.demo.model.Profile;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.ProfileRepository;

@Service
public class AuthService {

    @Autowired
    private StudentRepository repo;

    @Autowired
    private ProfileRepository profileRepo;

    // ✅ REGISTER
    public Student register(Student student){

        if(repo.findByEmail(student.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        if(student.getRole() == null){
            student.setRole("USER");
        }

        Student savedStudent = repo.save(student);

        // ✅ SAFE PROFILE CREATION
        Profile profile = new Profile();
        profile.setStudent(savedStudent);
        profile.setSkills("");
        profile.setTargetCompany("");
        profile.setRole("");
        profile.setPreferredDomain("");

        profileRepo.save(profile);

        return savedStudent;
    }

    // ✅ LOGIN (ADMIN + USER)
    public Student login(String email, String password){

        // 🔥 ADMIN LOGIN (NO DB)
        if(email.equals("admin@gmail.com") && password.equals("admin123")){
            Student admin = new Student();
            admin.setId(-1L); // IMPORTANT
            admin.setName("Admin");
            admin.setEmail(email);
            admin.setPassword(password);
            admin.setRole("ADMIN");
            return admin;
        }

        // ✅ NORMAL USER
        Student s = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!s.getPassword().equals(password)){
            throw new RuntimeException("Invalid password");
        }

        return s;
    }
}