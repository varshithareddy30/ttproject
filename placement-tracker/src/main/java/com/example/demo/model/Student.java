package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"goals"})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Name is required")
    private String name;

    @Email
    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="Password is required")
    private String password;

    @NotBlank(message="Branch is required")
    private String branch;

    @NotNull
    private Double cgpa;

    private String role;   // USER or ADMIN


    // Relationship with Goal
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals;


    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getBranch() { return branch; }
    public Double getCgpa() { return cgpa; }
    public String getRole() { return role; }
    public List<Goal> getGoals() { return goals; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }
    public void setRole(String role) { this.role = role; }
    public void setGoals(List<Goal> goals) { this.goals = goals; }

}