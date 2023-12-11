package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findAllByUserAndDeletedAtIsNull(UserEntity user);
    List<MemberEntity> findAllByCourseAndRoleAndDeletedAtIsNull(CourseEntity course, String role);
    Optional<MemberEntity> findByUserAndCourseAndRoleAndDeletedAtIsNull(UserEntity user, CourseEntity course, String role);
    Optional<MemberEntity> findByUserAndCourseAndDeletedAtIsNull(UserEntity user, CourseEntity course);
}
