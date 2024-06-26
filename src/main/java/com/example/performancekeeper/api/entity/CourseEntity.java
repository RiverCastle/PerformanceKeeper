package com.example.performancekeeper.api.entity;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE course_entity SET deleted_at = NOW() WHERE id = ?")

public class CourseEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String joinCode;
    private String description;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MemberEntity> members = new ArrayList<>();

    public void add(MemberEntity member) {
        member.setCourse(this);
        this.members.add(member);
    }

    public String getName() {
        return name;
    }
}
