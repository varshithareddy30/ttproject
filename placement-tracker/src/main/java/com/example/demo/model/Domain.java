package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    public Long getId() { return id; }

    public String getName() { return name; }

    public Student getStudent() { return student; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setStudent(Student student) { this.student = student; }
}