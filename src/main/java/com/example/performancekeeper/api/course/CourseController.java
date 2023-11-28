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
}
