package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Goal;

public interface GoalRepository extends JpaRepository<Goal, Long>{

    List<Goal> findByStudentId(Long studentId);
    void deleteByStudentId(Long studentId);
    List<Goal>findByGlobalGoalTrue();
}