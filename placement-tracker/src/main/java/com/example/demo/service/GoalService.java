package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Goal;
import com.example.demo.model.Student;
import com.example.demo.repository.GoalRepository;
import com.example.demo.repository.StudentRepository;

@Service
public class GoalService {

    @Autowired
    private GoalRepository repo;

    @Autowired
    private StudentRepository studentRepo;

    // ✅ CREATE GOAL (USER GOAL ONLY)
    public Goal createGoal(Goal goal){

        // 🔥 If global goal → no student needed
        if(goal.isGlobalGoal()){
            return repo.save(goal);
        }

        // ✅ normal user goal
        if(goal.getStudent() == null || goal.getStudent().getId() == null){
            throw new RuntimeException("Student ID missing");
        }

        Long studentId = goal.getStudent().getId();

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        goal.setStudent(student);

        if(goal.getCompleted() == null){
            goal.setCompleted(0);
        }

        return repo.save(goal);
    }

    // ✅ ONLY USER GOALS (NO GLOBAL HERE ❗)
    public List<Goal> getStudentGoals(Long studentId){
        return repo.findByStudentId(studentId);
    }

    // ✅ GLOBAL GOALS
    public List<Goal> getGlobalGoals(){
        return repo.findByGlobalGoalTrue();
    }

    // ✅ DELETE GOAL
    public void deleteGoal(Long id){
        if(!repo.existsById(id)){
            throw new RuntimeException("Goal not found");
        }
        repo.deleteById(id);
    }

    // ✅ GET SINGLE GOAL
    public Goal getGoalById(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    // ✅ UPDATE / SAVE
    public Goal saveGoal(Goal goal){
        return repo.save(goal);
    }
}