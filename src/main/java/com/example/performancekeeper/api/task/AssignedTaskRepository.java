package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedTaskRepository extends JpaRepository<AssignedTaskEntity, Long> {
    List<AssignedTaskEntity> findAllByTaskAndDeletedAtIsNull(TaskEntity task);
    List<AssignedTaskEntity> findAllByMemberAndDeletedAtIsNull(MemberEntity member);
    AssignedTaskEntity findByMemberAndTaskAndDeletedAtIsNull(MemberEntity member, TaskEntity task);
    List<AssignedTaskEntity> findAllAssignedTaskEntitiesByTaskAndDeletedAtIsNull(TaskEntity task);
    Optional<AssignedTaskEntity> findByIdAndDeletedAtIsNull(Long id);
}
