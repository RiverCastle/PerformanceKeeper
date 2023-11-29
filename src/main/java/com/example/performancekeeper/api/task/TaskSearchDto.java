package com.example.performancekeeper.api.task;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskSearchDto {
    private String condition;
    private String keyword;
    private LocalDate date;
}
