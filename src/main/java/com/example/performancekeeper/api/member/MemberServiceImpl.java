package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.users.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl {
    private final MemberRepository memberRepository;

    public void checkManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Manager")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_MANAGER));
    }
    public List<MemberEntity> getAllStudentsOfThisCourse(CourseEntity course) {
        return memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(course, "Student");
    }

    public MemberEntity checkStudentMember(UserEntity user, CourseEntity course) {
        return memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_STUDENT));
    }
}
