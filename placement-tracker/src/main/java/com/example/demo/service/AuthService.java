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
    StudentRepository repo;

    @Autowired
    ProfileRepository profileRepo;   // ✅ ADD THIS

    public Student register(Student student){

        if(repo.findByEmail(student.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        if(student.getRole() == null){
            student.setRole("USER");
        }

        // ✅ SAVE STUDENT FIRST
        Student savedStudent = repo.save(student);

        // ✅ CREATE PROFILE AUTOMATICALLY
        Profile profile = new Profile();
        profile.setStudent(savedStudent);

        // default empty values (so UI won't crash)
        profile.setSkills("");
        profile.setTargetCompany("");
        profile.setRole("");
        profile.setPreferredDomain("");

        profileRepo.save(profile);

        return savedStudent;
    }

    public Student login(String email,String password){

        Student s = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!s.getPassword().equals(password)){
            throw new RuntimeException("Invalid password");
        }

        return s;
    }
}