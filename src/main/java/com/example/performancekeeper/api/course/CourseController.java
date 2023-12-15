package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.common.PerformanceKeeperFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {
    private final PerformanceKeeperFacade performanceKeeperFacade;
    @PostMapping
    public void createCourse(Authentication authentication,
                             @RequestBody CourseCreateDto courseCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createCourse(userId, courseCreateDto);
    }
    @GetMapping
    public List<CourseOverviewDto> getCourseList(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        return performanceKeeperFacade.searchCourse(keyword);
    }

    @GetMapping("/myCourse")
    public List<MyCourseOverviewDto> getMyCourses(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getMyCourses(userId);
    }

    @GetMapping("/{courseId}")
    public CourseDetailsDto getCourseDetails(Authentication authentication,
                                             @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getCourseDetails(userId, courseId);
    }

    @PutMapping("/{courseId}/name")
    public void updateCourseName(Authentication authentication,
                                 @PathVariable("courseId") Long courseId,
                                 @RequestBody CourseNameUpdateDto courseNameUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.updateCourseName(userId, courseId, courseNameUpdateDto);
    }

    @PutMapping("/{courseId}/description")
    public void updateDescriptionName(Authentication authentication,
                                      @PathVariable("courseId") Long courseId,
                                      @RequestBody CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.updateCourseDescription(userId, courseId, courseDescriptionUpdateDto);
    }
}
