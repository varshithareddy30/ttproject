package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    void deleteByStudentId(Long studentId);
    void deleteByGoal_Id(Long goalId);
    Progress findByStudent_IdAndGoal_Id(Long studentId, Long goalId);

}