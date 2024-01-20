package com.example.Hotel.service.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Hotel.entity.task.TaskConfig;
import com.example.Hotel.repository.task.TaskConfigRepository;

@Service
public class TaskConfigService {
    
    @Autowired
    TaskConfigRepository taskConfigRepository;

    //add task name
    public String create(String name)
    {
        TaskConfig t = new TaskConfig();
        t.setTaskName(name);
        taskConfigRepository.save(t);
        return "Task Type "+ name + " added!";
    }

    //delete
    public String delete(String taskName)
    {
        taskConfigRepository.deleteByTaskName(taskName);
        return "Task Type: "+ taskName + " deleted!";
    }
    
    //get task list
    public List<String> getAll()
    {
        List<TaskConfig> list = taskConfigRepository.findAll();
        List<String> result = new ArrayList<>();
        for(TaskConfig t : list)
        {
            result.add(t.getTaskName());
        }
        return result;
    }

}
