package com.example.performancekeeper.api.facade;

import com.example.performancekeeper.api.dto.member.JoinCourseDto;
import com.example.performancekeeper.api.dto.member.LeaveRequestDto;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.dto.member.NicknameUpdateDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberControllerFacade {
    private final UserService userService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TaskService taskService;

    @Transactional
    public void createStudentMember(Long userId, Long courseId, JoinCourseDto joinCourseDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.createStudentMember(user, course, joinCourseDto);
        taskService.assignTasksToNewStudent(course, member); // 새 학생에게 기존의 과제물 부여
    }

    @Transactional
    public void deleteStudentMember(Long userId, Long courseId, LeaveRequestDto leaveRequestDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkStudentMemberBeforeDelete(user, course, leaveRequestDto);
        taskService.deleteAssignedTasksOfLeavingStudent(member);
        memberService.deleteStudentMember(member);
    }

    public void changeNickname(Long userId, Long courseId, NicknameUpdateDto nicknameUpdateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        memberService.changeNickname(member, nicknameUpdateDto);
    }

    public MemberOverviewDto getMemberInfo(Long userId, Long courseId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        return memberService.getMemberInfo(member);
    }
}
