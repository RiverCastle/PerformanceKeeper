package com.example.performancekeeper.api.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    List<CourseEntity> findAllByNameContainingAndDeletedAtIsNull(String keyword);

    Optional<CourseEntity> findByIdAndDeletedAtIsNull(Long courseId);
}
