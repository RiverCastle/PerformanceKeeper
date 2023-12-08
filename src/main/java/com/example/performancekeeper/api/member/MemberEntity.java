package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.users.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
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
    @Setter
    @NotBlank
    private String nickname;

    public MemberEntity(UserEntity user, CourseEntity course, String role) {
        this.user = user;
        this.course = course;
        this.role = role;
        this.nickname = getUser().getUsername();
    }
}
