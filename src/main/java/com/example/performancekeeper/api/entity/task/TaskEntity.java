package com.example.performancekeeper.api.entity.task;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.CourseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TaskEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDate startAt;
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;
}
