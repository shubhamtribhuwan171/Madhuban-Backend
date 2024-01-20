package com.example.Hotel.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.task.TaskConfig;
import com.example.Hotel.repository.task.TaskConfigRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class DataInitialisationService {
    
    @Autowired
    TaskConfigRepository taskConfigRepository;

    @PostConstruct
    @Transactional
    public void initializeDataIfNeeded() {
        if (!dataAlreadyInitialized()) {
            initializeData();
        }
        
    }

    private boolean dataAlreadyInitialized() {
        // Check if a sample room exists
        int size =  taskConfigRepository.findAll().size();
        if(size > 0)
            return true;
        else
            return false;
    }

    private void initializeData() {
        String taskName = "Cleaning";
        TaskConfig t = new TaskConfig();
        t.setTaskName(taskName);
        taskConfigRepository.save(t);
    }
}
