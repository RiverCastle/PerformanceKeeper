package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.course.CourseCreateDto;
import com.example.performancekeeper.api.facade.CourseControllerFacade;
import com.example.performancekeeper.api.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
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
    void createCourse() {
        CourseCreateDto dto = new CourseCreateDto();
        dto.setName("test course");
        dto.setJoinCode("test course joincode");
        dto.setDescription("This is a test course");
        courseController.createCourse(testAuthentication, dto);
    }

    @Test
    void getCourseList() {
    }

    @Test
    void getMyCourses() {
    }

    @Test
    void getCourseDetails() {
    }

    @Test
    void updateCourseName() {
    }

    @Test
    void updateDescriptionName() {
    }
}