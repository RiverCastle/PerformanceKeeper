package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.facade.CourseControllerFacade;
import com.example.performancekeeper.api.dto.course.*;
import com.example.performancekeeper.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 강의실 관련 Controller입니다.
 * 강의실 생성, 검색, 조회, 수정 요청을 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;
    private final CourseControllerFacade facade;

    /**
     * 강의실 생성 요청을 처리하는 메서드입니다.
     *
     * @param authentication
     * @param courseCreateDto 강의실 생성을 윙한 데이터를 담은 Dto
     */
    @PostMapping
    public void createCourse(Authentication authentication,
                             @RequestBody CourseCreateDto courseCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        facade.createCourse(userId, courseCreateDto);
    }

    /**
     * 현재 생성된 강의실들을 조회하는 메서드입니다.
     * 입력된 키워드를 포함하는 강의실 명이 조회됩니다.
     *
     * @param keyword
     * @return 현재 생성된 강의실들의 대략적인 정보가 담긴 Dto객체들이 담긴 리스트 반환
     */
    @GetMapping
    public List<CourseOverviewDto> getCourseList(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return courseService.searchCourse(keyword);
    }

    /**
     * 자신이 속한 강의실만 조회하는 요청을 처리하는 메서드입니다.
     *
     * @param authentication
     * @return 자신이 속한 강의실의 대략적인 정보가 포함된 Dto객체들이 담긴 리스트 반환
     */
    @GetMapping("/myCourse")
    public List<MyCourseOverviewDto> getMyCourses(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return facade.getMyCourses(userId);
    }

    /**
     * 자신이 속한 특정 강의실을 조회하는 요청을 처리하는 메서드입니다.
     *
     * @param authentication
     * @param courseId 조회할 강의실의 id
     * @return 해당 강의실의 세부적인 정보를 담은 Dto객체 반환
     */
    @GetMapping("/{courseId}")
    public CourseDetailsDto getCourseDetails(Authentication authentication,
                                             @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return facade.getCourseDetails(userId, courseId);
    }

    /**
     * 강의실 이름을 수정하는 요청을 처리하는 메서드입니다.
     *
     * @param authentication
     * @param courseId
     * @param courseNameUpdateDto 새로운 강의실 이름이 담긴 dto객체
     */
    @PutMapping("/{courseId}/name")
    public void updateCourseName(Authentication authentication,
                                 @PathVariable("courseId") Long courseId,
                                 @RequestBody CourseNameUpdateDto courseNameUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        facade.updateCourseName(userId, courseId, courseNameUpdateDto);
    }

    /**
     * 강의실 소개를 수정하는 요청을 처리하는 메서드입니다.
     *
     * @param authentication
     * @param courseId
     * @param courseDescriptionUpdateDto 새로운 강의실 소개내용이 담긴 dto객체
     */
    @PutMapping("/{courseId}/description")
    public void updateDescriptionName(Authentication authentication,
                                      @PathVariable("courseId") Long courseId,
                                      @RequestBody CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        facade.updateCourseDescription(userId, courseId, courseDescriptionUpdateDto);
    }
}
