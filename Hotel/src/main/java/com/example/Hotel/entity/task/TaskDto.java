package com.example.Hotel.entity.task;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TaskDto {
    Long taskId;

    LocalDate dueDate;
    LocalTime dueTime;

    String taskName;

    String priority;

    Integer roomNumber;

    String status;
    boolean autoGenerated;// yes/ no

}