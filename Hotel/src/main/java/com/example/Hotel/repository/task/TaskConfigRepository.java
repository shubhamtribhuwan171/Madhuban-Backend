package com.example.Hotel.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Hotel.entity.task.TaskConfig;


@Repository
public interface TaskConfigRepository extends JpaRepository<TaskConfig, Long>{

    TaskConfig findByTaskName(String taskName);
    void deleteByTaskName(String taskName);
}
