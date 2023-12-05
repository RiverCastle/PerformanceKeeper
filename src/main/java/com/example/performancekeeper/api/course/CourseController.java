package com.example.performancekeeper.api.course;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;
    @PostMapping
    public void createCourse(Authentication authentication,
                             @RequestBody CourseCreateDto courseCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        courseService.createCourse(userId, courseCreateDto);
    }
    @GetMapping
    public List<CourseOverviewDto> getCourseList(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return courseService.searchCourse(keyword);
    }

    @GetMapping("/myCourse")
    public List<MyCourseOverviewDto> getMyCourses(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return courseService.getMyCourses(userId);
    }

    @GetMapping("/{courseId}")
    public CourseDetailsDto getCourseDetails(Authentication authentication,
                                             @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return courseService.getCourseDetails(userId, courseId);
    }

}
