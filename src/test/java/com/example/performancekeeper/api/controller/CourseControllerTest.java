package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.course.CourseCreateDto;
import com.example.performancekeeper.api.dto.course.CourseDescriptionUpdateDto;
import com.example.performancekeeper.api.dto.course.CourseNameUpdateDto;
import com.example.performancekeeper.api.facade.CourseControllerFacade;
import com.example.performancekeeper.api.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {
    @InjectMocks
    private CourseController courseController;
    @Mock
    private CourseService courseService;
    @Mock
    private CourseControllerFacade facade;
    private MockMvc mockMvc;
    private Authentication testAuthentication;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        Long testUserId = 1L;
        testAuthentication = new TestingAuthenticationToken(testUserId, "test password", "USER_ROLE");
        testAuthentication.setAuthenticated(true);
    }
    @Test
    @DisplayName("강의실 생성 요청")
    void createCourse() {
        CourseCreateDto dto = new CourseCreateDto();
        dto.setName("test course");
        dto.setJoinCode("test course joincode");
        dto.setDescription("This is a test course");
        courseController.createCourse(testAuthentication, dto);
    }

    @Test
    @DisplayName("강의실 목록 조회 요청")
    void getCourseList() {
        String keyword = "";
        courseController.getCourseList(keyword);
    }

    @Test
    @DisplayName("나의 강의실 목록 조회 요청")
    void getMyCourses() {
        courseController.getMyCourses(testAuthentication);
    }

    @Test
    @DisplayName("강의실 세부 정보 조회 요청")
    void getCourseDetails() {
        Long testCourseId = 1L;
        courseController.getCourseDetails(testAuthentication, testCourseId);
    }

    @Test
    @DisplayName("강의실 명 수정 요청")
    void updateCourseName() {
        Long testCourseId = 1L;
        CourseNameUpdateDto dto = new CourseNameUpdateDto();
        dto.setNewName("new course name");

        courseController.updateCourseName(testAuthentication, testCourseId, dto);
    }

    @Test
    @DisplayName("강의실 소개 수정 요청")
    void updateDescriptionName() {
        Long testCourseId = 1L;
        CourseDescriptionUpdateDto dto = new CourseDescriptionUpdateDto();
        dto.setNewDescription("new course desc");

        courseController.updateDescriptionName(testAuthentication, testCourseId, dto);
    }
}