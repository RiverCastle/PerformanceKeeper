package com.example.performancekeeper;

import com.example.performancekeeper.api.common.TimeTracer;
import com.example.performancekeeper.api.facade.CourseControllerFacade;
import com.example.performancekeeper.api.facade.MemberControllerFacade;
import com.example.performancekeeper.api.facade.TaskControllerFacade;
import com.example.performancekeeper.api.service.CommentService;
import com.example.performancekeeper.api.facade.CommentControllerFacade;
import com.example.performancekeeper.api.service.CourseService;
import com.example.performancekeeper.api.service.MemberService;
import com.example.performancekeeper.api.service.TaskService;
import com.example.performancekeeper.api.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommentControllerFacade commentControllerFacade(
            UserService userService,
            CourseService courseService,
            MemberService memberService,
            TaskService taskService,
            CommentService commentService) {
        return new CommentControllerFacade(userService, courseService, memberService, taskService, commentService);
    }
    @Bean
    public CourseControllerFacade courseControllerFacade(UserService userService,
                                                         CourseService courseService,
                                                         MemberService memberService,
                                                         TaskService taskService) {
        return new CourseControllerFacade(userService, courseService, memberService, taskService);
    }

    @Bean
    public MemberControllerFacade memberControllerFacade(UserService userService,
                                                         CourseService courseService,
                                                         MemberService memberService,
                                                         TaskService taskService) {
        return new MemberControllerFacade(userService, courseService, memberService, taskService);
    }

    @Bean
    public TaskControllerFacade taskControllerFacade(UserService userService,
                                                     CourseService courseService,
                                                     MemberService memberService,
                                                     TaskService taskService) {
        return new TaskControllerFacade(userService, courseService, memberService, taskService);
    }
    @Bean
    public TimeTracer TimeTracer() {
        return new TimeTracer();
    }
}
