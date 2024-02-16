package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.course.*;
import com.example.performancekeeper.api.entity.CourseEntity;

import java.util.List;

public interface CourseService {
    CourseEntity createCourse(CourseCreateDto courseCreateDto);
    List<CourseOverviewDto> searchCourse(String keyword);
    CourseDetailsDto getCourseDetails(CourseEntity course);
    void updateCourseName(CourseEntity course, CourseNameUpdateDto courseNameUpdateDto);
    void updateCourseDescription(CourseEntity course, CourseDescriptionUpdateDto courseDescriptionUpdateDto);
    CourseEntity checkCourseEntity(Long courseId);

    CourseEntity checkCourse(Long courseId);
}
