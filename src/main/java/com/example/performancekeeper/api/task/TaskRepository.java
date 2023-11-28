package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.course.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByCourseAndDeletedAtIsNull(CourseEntity courseEntity);
}
