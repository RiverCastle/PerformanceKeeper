package com.example.performancekeeper.api.repository;

import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByAssignedTaskEntity(AssignedTaskEntity assignedTask);
    Optional<CommentEntity> findByIdAndDeletedAtIsNull(Long commentId);
    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.replies WHERE c.assignedTaskEntity = :assignedTaskEntity")
    List<CommentEntity> findAllByAssignedTaskEntityWithReplies(@Param("assignedTaskEntity") AssignedTaskEntity assignedTask);
}
