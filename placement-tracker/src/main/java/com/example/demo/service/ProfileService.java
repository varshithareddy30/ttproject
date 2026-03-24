package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Profile;
import com.example.demo.model.Student;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.StudentRepository;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository repo;

    @Autowired
    StudentRepository studentRepo;   // 🔥 ADD THIS

    public Profile createProfile(Profile profile){

        Long studentId = profile.getStudent().getId();

        Optional<Profile> existing = repo.findByStudentId(studentId);

        if(existing.isPresent()){
            return existing.get();   // ✅ FIXED
        }

        return repo.save(profile);
    }

    public Profile getProfile(Long studentId){

        return repo.findByStudentId(studentId)
                .orElse(null);

    }
    public Profile updateProfile(Profile profile){

        Long studentId = profile.getStudent().getId();

        Profile existing = repo.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // ✅ update fields
        existing.setSkills(profile.getSkills());
        existing.setTargetCompany(profile.getTargetCompany());
        existing.setRole(profile.getRole());
        existing.setPreferredDomain(profile.getPreferredDomain());

        return repo.save(existing);
    }
}