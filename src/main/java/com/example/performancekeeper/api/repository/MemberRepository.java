package com.example.performancekeeper.api.repository;

import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Query("select m from MemberEntity m join fetch m.course join fetch m.user where m.user = :user and m.deletedAt is null")
    List<MemberEntity> findAllByUserAndDeletedAtIsNull(@Param("user") UserEntity user);
    List<MemberEntity> findAllByCourseAndRoleAndDeletedAtIsNull(CourseEntity course, String role);
    Optional<MemberEntity> findByUserAndCourseAndRoleAndDeletedAtIsNull(UserEntity user, CourseEntity course, String role);
    Optional<MemberEntity> findByUserAndCourseAndDeletedAtIsNull(UserEntity user, CourseEntity course);
}
