package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.task.AssignedTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByAssignedTaskEntity(AssignedTaskEntity assignedTask);
}
