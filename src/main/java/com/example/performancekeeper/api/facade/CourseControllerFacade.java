package com.example.performancekeeper.api.facade;

import com.example.performancekeeper.api.dto.course.*;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.service.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CourseControllerFacade {
    private final UserService userService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TaskService taskService;

    public void createCourse(Long userId, CourseCreateDto courseCreateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.createCourse(courseCreateDto);
        memberService.createManagerMember(user, course);
    }

    public List<MyCourseOverviewDto> getMyCourses(Long userId) {
        UserEntity user = userService.checkUser(userId);
        List<MyCourseOverviewDto> myCoursesDtoList = new ArrayList<>();
        List<MemberEntity> myMemberList = memberService.getMyMember(user);
        for (MemberEntity memberEntity : myMemberList) {
            CourseEntity course = memberEntity.getCourse();
            MyCourseOverviewDto myCourseOverviewDto = new MyCourseOverviewDto(course.getId(), course.getName(), memberEntity.getRole());
            myCourseOverviewDto.setProgress(taskService.getProgress(memberEntity));
            myCoursesDtoList.add(myCourseOverviewDto);
        }
        return myCoursesDtoList;
    }

    public CourseDetailsDto getCourseDetails(Long userId, Long courseId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkMember(user, course); // 가입 여부 파악을 위해 할당 필요X
        return courseService.getCourseDetails(course);
    }

    public void updateCourseName(Long userId, Long courseId, CourseNameUpdateDto courseNameUpdateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        courseService.updateCourseName(course, courseNameUpdateDto);
    }

    public void updateCourseDescription(Long userId, Long courseId, CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        courseService.updateCourseDescription(course, courseDescriptionUpdateDto);
    }

}
