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

    MemberEntity createStudentMember(UserEntity user, CourseEntity course, JoinCourseDto joinCourseDto);

    List<MemberEntity> getMyMember(UserEntity user);

    void deleteStudentMember(MemberEntity member);

    void changeNickname(MemberEntity member, NicknameUpdateDto nicknameUpdateDto);

    MemberOverviewDto getMemberInfo(MemberEntity member);

    MemberEntity checkStudentMemberBeforeDelete(UserEntity user, CourseEntity course, LeaveRequestDto leaveRequestDto);
}
