package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.users.UserEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private CourseEntity course;
    private String role;

    public MemberEntity(UserEntity user, CourseEntity course, String role) {
        this.user = user;
        this.course = course;
        this.role = role;
    }
}
