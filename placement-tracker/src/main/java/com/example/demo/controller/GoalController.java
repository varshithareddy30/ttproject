package com.example.demo.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Goal;
import com.example.demo.model.Progress;
import com.example.demo.model.Student;
import com.example.demo.service.GoalService;
import com.example.demo.repository.ProgressRepository;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/goal")
@CrossOrigin(origins="*")
public class GoalController {

    @Autowired
    GoalService service;

    @Autowired
    ProgressRepository progressRepo;

    @Autowired
    StudentRepository studentRepo;

    // ✅ CREATE USER GOAL
    @PostMapping("/create")
    public Goal createGoal(@RequestBody Goal goal){
        goal.setGlobalGoal(false);
        return service.createGoal(goal);
    }

    // ✅ USER GOALS ONLY
    @GetMapping("/student/{id}")
    public List<Goal> getStudentGoals(@PathVariable Long id){
        return service.getStudentGoals(id);
    }

    // ✅ USER + GLOBAL GOALS
    @GetMapping("/all/{id}")
    public List<Goal> getAllGoals(@PathVariable Long id){

        List<Goal> userGoals = service.getStudentGoals(id);
        List<Goal> globalGoals = service.getGlobalGoals();

        List<Goal> allGoals = new ArrayList<>();
        allGoals.addAll(userGoals);
        allGoals.addAll(globalGoals);

        return allGoals;
    }

    // ✅ DELETE GOAL
    @DeleteMapping("/{id}")
    public String deleteGoal(@PathVariable Long id){
        service.deleteGoal(id);
        return "Goal Deleted";
    }

    // 🔥 FIXED: SAVE SCORE PER USER
    @PutMapping("/updateScore/{goalId}/{score}/{studentId}")
    public String updateScore(
            @PathVariable Long goalId,
            @PathVariable int score,
            @PathVariable Long studentId) {

        Goal goal = service.getGoalById(goalId);

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // ✅ SAVE IN PROGRESS (correct logic)
        Progress progress = new Progress();
        progress.setGoal(goal);
        progress.setStudent(student);
        progress.setScore(score);
        progressRepo.save(progress);
     // 🔥 ALSO UPDATE GOAL FOR UI
        int newCompleted = goal.getCompleted() + score;

        if(newCompleted > goal.getTarget()){
            newCompleted = goal.getTarget();
        }

        goal.setCompleted(newCompleted);
        service.saveGoal(goal);

        // 🔥 ALSO UPDATE GOAL (UI fix)
        

        return "Score saved";
    }
    @GetMapping("/withProgress/{studentId}")
    public List<Goal> getGoalsWithProgress(@PathVariable Long studentId){

        List<Goal> goals = service.getGlobalGoals();
        List<Goal> userGoals = service.getStudentGoals(studentId);

        goals.addAll(userGoals);

        for(Goal g : goals){

            Progress p = progressRepo.findByStudent_IdAndGoal_Id(studentId, g.getId());

            if(p != null){
                g.setCompleted(p.getScore()); // 🔥 attach user score
            } else {
                g.setCompleted(0);
            }
        }

        return goals;
    }
}