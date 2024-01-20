package com.example.Hotel.controller.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.service.task.DataInitialisationService;
import com.example.Hotel.service.task.TaskConfigService;

@RestController
@RequestMapping("/taskConfig")
public class TaskConfigController {
    
    @Autowired
    TaskConfigService taskConfigService;

    @Autowired
    DataInitialisationService dataInitService;

    @GetMapping("/load")
    public ResponseEntity<String> initializeDataIfNeeded() {
        dataInitService.initializeDataIfNeeded();
        return ResponseEntity.ok("Data initialization completed or already initialized.");
    }

    //add task name
    @PostMapping("/create")
    public ResponseEntity<?> addTaskType(@RequestParam String name)
    {
        String response = taskConfigService.create(name);
        return ResponseEntity.ok(response);
    }
    //delete
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTaskType(@RequestParam String name)
    {
        String response = taskConfigService.delete(name);
        return ResponseEntity.ok(response);
    }
    
    //get task list
    @GetMapping("/getAll")
    public ResponseEntity<List<String>> getAll()
    {
        List<String> response = taskConfigService.getAll();
        return ResponseEntity.ok(response);
    }

}
