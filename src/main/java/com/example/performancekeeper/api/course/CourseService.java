package com.example.performancekeeper.api.course;

import java.util.List;

public interface CourseService {
    CourseEntity createCourse(CourseCreateDto courseCreateDto);
    List<CourseOverviewDto> searchCourse(String keyword);
    List<MyCourseOverviewDto> getMyCourses(Long userId);
    CourseDetailsDto getCourseDetails(Long userId, Long courseId);
    void updateCourseName(Long userId, Long courseId, CourseNameUpdateDto courseNameUpdateDto);
    void updateDescriptionName(Long userId, Long courseId, CourseDescriptionUpdateDto courseDescriptionUpdateDto);
    CourseEntity checkCourseEntity(Long courseId);

    CourseEntity checkCourse(Long courseId);
}
