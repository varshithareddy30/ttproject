package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skills;

    private String targetCompany;

    private String role;

    private String preferredDomain;

    @OneToOne
    @JoinColumn(name="student_id", unique = true)
    private Student student;

    public Long getId() {
        return id;
    }

    public String getSkills() {
        return skills;
    }

    public String getTargetCompany() {
        return targetCompany;
    }

    public String getRole() {
        return role;
    }

    public String getPreferredDomain() {
        return preferredDomain;
    }

    public Student getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setTargetCompany(String targetCompany) {
        this.targetCompany = targetCompany;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPreferredDomain(String preferredDomain) {
        this.preferredDomain = preferredDomain;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}