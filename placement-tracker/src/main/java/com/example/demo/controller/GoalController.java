package com.example.demo.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Goal;
import com.example.demo.service.GoalService;

@RestController
@RequestMapping("/goal")
@CrossOrigin(origins="http://localhost:3000")
public class GoalController {

    @Autowired
    GoalService service;

    // ✅ CREATE USER GOAL
    @PostMapping("/create")
    public Goal createGoal(@RequestBody Goal goal){
        goal.setGlobalGoal(false); // 🔥 important
        return service.createGoal(goal);
    }

    // ✅ GET ONLY USER GOALS (OLD)
    @GetMapping("/student/{id}")
    public List<Goal> getStudentGoals(@PathVariable Long id){
        return service.getStudentGoals(id);
    }

    // ✅ NEW: GET USER + GLOBAL GOALS
    @GetMapping("/all/{id}")
    public List<Goal> getAllGoals(@PathVariable Long id){

        List<Goal> userGoals = service.getStudentGoals(id);
        List<Goal> globalGoals = service.getGlobalGoals();

        List<Goal> allGoals = new ArrayList<>();
        allGoals.addAll(userGoals);
        allGoals.addAll(globalGoals);

        return allGoals;
    }

    // ✅ DELETE (only user goals should be deleted from frontend)
    @DeleteMapping("/{id}")
    public String deleteGoal(@PathVariable Long id){

        service.deleteGoal(id);

        return "Goal Deleted";
    }

    // ✅ UPDATE SCORE
    @PutMapping("/updateScore/{goalId}/{score}")
    public Goal updateScore(@PathVariable Long goalId, @PathVariable int score) {

        Goal goal = service.getGoalById(goalId);

        int newCompleted = goal.getCompleted() + score;

        if(newCompleted > goal.getTarget()){
            newCompleted = goal.getTarget();
        }

        goal.setCompleted(newCompleted);

        return service.saveGoal(goal);
    }
}