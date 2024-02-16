package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.repository.CourseRepository;
import com.example.performancekeeper.api.dto.course.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    public CourseEntity createCourse(CourseCreateDto courseCreateDto) {
        courseCreateDto.setJoinCode(passwordEncoder.encode(courseCreateDto.getJoinCode())); // 가입코드 암호화
        return courseRepository.save(CourseCreateDto.toEntity(courseCreateDto));
    }

    public List<CourseOverviewDto> searchCourse(String keyword) {
        List<CourseEntity> courseEntities = courseRepository.findAllByNameContainingAndDeletedAtIsNull(keyword);
        List<CourseOverviewDto> result = new ArrayList<>();
        for (CourseEntity entity : courseEntities) result.add(CourseOverviewDto.fromEntity(entity));
        return result;
    }

    public CourseDetailsDto getCourseDetails(CourseEntity course) {
        return CourseDetailsDto.fromEntity(course);
    }

    public void updateCourseName(CourseEntity course, CourseNameUpdateDto courseNameUpdateDto) {
        course.setName(courseNameUpdateDto.getNewName());
        courseRepository.save(course);
    }

    public void updateCourseDescription(CourseEntity course, CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        course.setDescription(courseDescriptionUpdateDto.getNewDescription());
        courseRepository.save(course);
    }

    public CourseEntity checkCourseEntity(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }

    @Override
    public CourseEntity checkCourse(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }
}
