package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Student;
import com.example.demo.model.Goal;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.GoalService;
import com.example.demo.repository.GoalRepository;
import com.example.demo.repository.DomainRepository;
import com.example.demo.repository.ProgressRepository;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    GoalRepository goalRepo;

    @Autowired
    DomainRepository domainRepo;

    
    @Autowired
    private GoalService goalService;
    
    @Autowired
    ProgressRepository progressRepo;

    // ✅ GET ALL USERS
    @GetMapping("/users")
    public List<Student> getAllUsers(){
        return studentRepo.findAll();
    }

    // ✅ GET USER GOALS
    @GetMapping("/goals/{id}")
    public List<Goal> getUserGoals(@PathVariable Long id){
    	List<Goal> personal=goalRepo.findByStudentId(id);
    	List<Goal> global=goalRepo.findByGlobalGoalTrue();
    	personal.addAll(global);
        return personal;
    }

    // ✅ DELETE USER
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id){

        goalRepo.deleteByStudentId(id);
        domainRepo.deleteByStudentId(id);
        progressRepo.deleteByStudentId(id);

        studentRepo.deleteById(id);

        return "User deleted successfully";
    }
    @PostMapping("/createGlobalGoal")
    public Goal createGlobalGoal(@RequestBody Goal goal){

        goal.setGlobalGoal(true);   // IMPORTANT
        goal.setStudent(null);      // no owner

        return goalService.saveGoal(goal);
    }
}