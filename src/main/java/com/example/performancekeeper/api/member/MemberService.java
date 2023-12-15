package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.users.UserEntity;

import java.util.List;

public interface MemberService {
    void checkManagerMember(UserEntity user, CourseEntity course);
    List<MemberEntity> getAllStudentsOfThisCourse(CourseEntity course);
    MemberEntity checkStudentMember(UserEntity user, CourseEntity course);
    MemberEntity checkMember(UserEntity user, CourseEntity course);
    void createManagerMember(UserEntity user, CourseEntity course);
    void createStudentMember(Long userId, Long courseId, JoinCourseDto joinCourseDto);
    List<MemberEntity> getMyMember(UserEntity user);
    void deleteStudentMember(Long userId, Long courseId, LeaveRequestDto leaveRequestDto);
    void changeNickname(Long userId, Long courseId, NicknameUpdateDto nicknameUpdateDto);
    MemberOverviewDto getMemberInfo(Long userId, Long courseId);
}
