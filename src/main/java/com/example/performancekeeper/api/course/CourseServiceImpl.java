package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl {
    private final CourseRepository courseRepository;


    public CourseEntity checkCourseEntity(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }
}
