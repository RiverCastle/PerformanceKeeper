package com.example.performancekeeper.api.entity;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE member_entity SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseEntity course;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<AssignedTaskEntity> tasks = new ArrayList<>();
    private String role;
    @Setter
    @NotBlank
    private String nickname;

    public MemberEntity(UserEntity user, CourseEntity course, String role) {
        this.user = user;
        this.course = course;
        this.role = role;
        this.nickname = this.user.getUsername();
    }
    public void addTask(AssignedTaskEntity task) {
        task.setMember(this);
        this.tasks.add(task);
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }
}
