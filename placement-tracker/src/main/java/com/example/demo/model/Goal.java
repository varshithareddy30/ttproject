package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean globalGoal;
    private String title;
    private String type;
    private Integer target;
    private Integer completed = 0;

    // ✅ FIXED HERE
    @ManyToOne
    @JoinColumn(name="student_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Student student;

    public Long getId(){ return id; }

    public String getTitle(){ return title; }

    public String getType(){ return type; }

    public Integer getTarget(){ return target; }

    public Integer getCompleted(){ return completed; }

    public Student getStudent(){ return student; }

    public void setTitle(String title){ this.title = title; }

    public void setType(String type){ this.type = type; }

    public void setTarget(Integer target){ this.target = target; }

    public void setCompleted(Integer completed){ this.completed = completed; }

    public void setStudent(Student student){ this.student = student; }
    
    public boolean isGlobalGoal() {
        return globalGoal;
    }

    public void setGlobalGoal(boolean globalGoal) {
        this.globalGoal = globalGoal;
    }
}