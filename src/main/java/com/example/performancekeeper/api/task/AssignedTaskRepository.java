package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignedTaskRepository extends JpaRepository<AssignedTaskEntity, Long> {
    List<AssignedTaskEntity> findAllByMemberAndDeletedAtIsNull(MemberEntity member);
    List<AssignedTaskEntity> findAllByMemberAndNameContainingAndDeletedAtIsNull(MemberEntity member, String keyword);
    List<AssignedTaskEntity> findAllByMemberAndStartAtAndDeletedAtIsNull(MemberEntity member, LocalDate date);
}
