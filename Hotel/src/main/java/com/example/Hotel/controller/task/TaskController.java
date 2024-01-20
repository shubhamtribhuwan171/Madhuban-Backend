package com.example.Hotel.controller.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Hotel.entity.task.TaskDto;
import com.example.Hotel.service.task.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    //to create a new task
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto)
    {
        String response = taskService.createTask(taskDto);
        return ResponseEntity.ok(response);
    }

    //get all the task
    @GetMapping("/getAll")
    public ResponseEntity<List<TaskDto>> getAll()
    {
        List<TaskDto> list = taskService.getAllTasks();
        return ResponseEntity.ok(list);
    }

    //update status
    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateTaskStatus(@RequestParam String newStatus, @RequestParam Long taskId)
    {
        String response = taskService.updateStatus(newStatus, taskId);
        return ResponseEntity.ok(response);
    }

    //get by id
    @GetMapping("/getById")
    public ResponseEntity<?> getByTaskId(@RequestParam Long id)
    {
        TaskDto response = taskService.getById(id);
        return ResponseEntity.ok(response);
    }

    //get incomplete task
    @GetMapping("/getByStatus")
    public ResponseEntity<List<TaskDto>> getByStatus(@RequestParam String status)
    {
        List<TaskDto> list = taskService.getByStatus(status);
        return ResponseEntity.ok(list);
    }
    
    //get task by due date
    @GetMapping("/getByDueDate")
    public ResponseEntity<List<TaskDto>> getByDueDate(@RequestParam LocalDate dueDate)
    {
        List<TaskDto> list = taskService.getTaskByDueDate(dueDate);
        return ResponseEntity.ok(list);
    }
    
    //get task by priority
    @GetMapping("/getByPriorityAndDueDate")
    public ResponseEntity<List<TaskDto>> getByPriority(@RequestParam String priority, @RequestParam(required = false) LocalDate dueDate)
    {

        if(dueDate == null)
            dueDate = LocalDate.now();
        List<TaskDto> list = taskService.getTaskByPriorityAndDueDate(priority, dueDate);
        return ResponseEntity.ok(list);
    }
    //edit task
    @PutMapping("/editTask")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto dto, @RequestParam Long taskId)
    {
        TaskDto task = taskService.updateTask(dto, taskId);
        return ResponseEntity.ok("Updated Task: " + task);
    }
    //delete task
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByTaskNameAndRoomNumber(@RequestParam String taskName, @RequestParam Integer roomNumber)
    {
        String response = taskService.deleteTask(taskName, roomNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByRoom")
    public ResponseEntity<List<TaskDto>> getByRoom(@RequestParam Integer roomNumber)
    {
        List<TaskDto> list = taskService.getByRoom(roomNumber);
        return ResponseEntity.ok(list);
    }

    
}
