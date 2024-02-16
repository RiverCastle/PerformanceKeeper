package com.example.performancekeeper.api.repository;

import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedTaskRepository extends JpaRepository<AssignedTaskEntity, Long> {
    List<AssignedTaskEntity> findAllByTaskAndDeletedAtIsNull(TaskEntity task);
    List<AssignedTaskEntity> findAllByMemberAndDeletedAtIsNull(MemberEntity member);
    AssignedTaskEntity findByMemberAndTaskAndDeletedAtIsNull(MemberEntity member, TaskEntity task);
    List<AssignedTaskEntity> findAllAssignedTaskEntitiesByTaskAndDeletedAtIsNull(TaskEntity task);
    Optional<AssignedTaskEntity> findByIdAndDeletedAtIsNull(Long id);
    List<AssignedTaskEntity> findAllByMemberAndStatusIsNotAndDeletedAtIsNull(MemberEntity member, String status);
}
