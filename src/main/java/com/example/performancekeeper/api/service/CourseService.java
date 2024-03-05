package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.course.*;
import com.example.performancekeeper.api.entity.CourseEntity;

import java.util.List;

public interface CourseService {
    /**
     * 새로운 강의실을 생성하는 메서드입니다.
     *
     * @param courseCreateDto
     * @return 새로 생성된 CourseEntity 객체를 반환
     */
    CourseEntity createCourse(CourseCreateDto courseCreateDto);

    /**
     * 키워드를 포함한 이름을 가진 강의실을 검색하는 메서드입니다.
     *
     * @param keyword 검색 키워드
     * @return 검색 결과 중 대략적인 정보들을 dto로 바꾸고 리스트에 담아 반환
     */
    List<CourseOverviewDto> searchCourse(String keyword);

    /**
     * 특정 강의실의 세부 정보를 조회하는 메서드입니다.
     *
     * @param course
     * @return 사용자에게 공개할 수 있는 세부정보만을 Dto에 담아 반환
     */
    CourseDetailsDto getCourseDetails(CourseEntity course);

    /**
     * 강의실 이름을 수정하는 메서드입니다.
     *
     * @param course
     * @param courseNameUpdateDto 새로운 이름이 담긴 dto 객체
     */
    void updateCourseName(CourseEntity course, CourseNameUpdateDto courseNameUpdateDto);

    /**
     * 강의실 소개내용을 수정하는 메서드입니다.
     *
     * @param course
     * @param courseDescriptionUpdateDto 새로운 소개 내용이 담긴 dto 객체
     */
    void updateCourseDescription(CourseEntity course, CourseDescriptionUpdateDto courseDescriptionUpdateDto);

    /**
     * 강의실 존재 여부를 파악하는 메서드입니다.
     *
     * @param courseId
     * @return 정상 수행 시, 해당 CourseEntity 반환
     * @deprecated
     */
    CourseEntity checkCourseEntity(Long courseId);
    /**
     * 강의실 존재 여부를 파악하는 메서드입니다.
     *
     * @param courseId
     * @return 정상 수행 시, 해당 CourseEntity 반환
     */
    CourseEntity checkCourse(Long courseId);
}
