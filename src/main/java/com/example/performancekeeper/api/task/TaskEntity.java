package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.member.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TaskEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;
    private String desc;
    @ManyToOne
    private CourseEntity course;
    @ManyToOne
    private MemberEntity member;

    public TaskEntity(String name, String status, String desc, CourseEntity course) {
        this.name = name;
        this.status = status;
        this.desc = desc;
        this.course = course;
    }

    public static TaskEntity fromEntity(TaskEntity entity) {
        return new TaskEntity(
                entity.getName(),
                entity.getStatus(),
                entity.desc,
                entity.getCourse());

    }
}
