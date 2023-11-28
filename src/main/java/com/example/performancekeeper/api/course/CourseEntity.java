package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class CourseEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String joinCode;
    private String desc;
}
