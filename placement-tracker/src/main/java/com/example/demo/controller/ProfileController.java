package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    @Autowired
    ProfileService service;

    @PostMapping("/create")
    public Profile createProfile(@RequestBody Profile profile){

        Profile existing = service.getProfile(profile.getStudent().getId());

        if(existing != null){
            return existing;   // 🚫 STOP duplicate creation
        }

        return service.createProfile(profile);
    }

    @GetMapping("/{studentId}")
    public Profile getProfile(@PathVariable Long studentId){
        return service.getProfile(studentId);
    }
    @PutMapping("/update")
    public Profile updateProfile(@RequestBody Profile profile){
        return service.updateProfile(profile);
    }
}