package com.example.demo.service;

import jakarta.annotation.PostConstruct;
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
    ProfileRepository profileRepo;

    // ✅ AUTO CREATE ADMIN (🔥 VERY IMPORTANT)
    @PostConstruct
    public void createAdmin(){

        if(repo.findByEmail("admin@gmail.com").isEmpty()){

            Student admin = new Student();

            admin.setName("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");

            repo.save(admin);

            System.out.println("✅ Admin created successfully");
        }
    }

    // ✅ REGISTER
    public Student register(Student student){

        if(repo.findByEmail(student.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }

        if(student.getRole() == null){
            student.setRole("USER");
        }

        // SAVE STUDENT
        Student savedStudent = repo.save(student);

        // CREATE PROFILE
        Profile profile = new Profile();
        profile.setStudent(savedStudent);

        profile.setSkills("");
        profile.setTargetCompany("");
        profile.setRole("");
        profile.setPreferredDomain("");

        profileRepo.save(profile);

        return savedStudent;
    }

    // ✅ LOGIN
    public Student login(String email,String password){

        Student s = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!s.getPassword().equals(password)){
            throw new RuntimeException("Invalid password");
        }

        return s;
    }
}