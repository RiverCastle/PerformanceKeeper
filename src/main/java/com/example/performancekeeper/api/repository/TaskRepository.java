package com.example.performancekeeper.api.repository;

import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByCourseAndDeletedAtIsNull(CourseEntity course);
    List<TaskEntity> findAllByCourseAndStartAtAndDeletedAtIsNull(CourseEntity course, LocalDate startAt);
    List<TaskEntity> findAllByCourseAndNameContainingAndDeletedAtIsNull(CourseEntity course, String keyword);
    Optional<TaskEntity> findByIdAndDeletedAtIsNull(Long taskId);
}
