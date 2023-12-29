package com.example.performancekeeper;

import com.example.performancekeeper.aop.TimeTracer;
import com.example.performancekeeper.api.comment.CommentService;
import com.example.performancekeeper.api.common.PerformanceKeeperFacade;
import com.example.performancekeeper.api.course.CourseService;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserService;
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
    public PerformanceKeeperFacade performanceKeeperFacade(
            UserService userService,
            CourseService courseService,
            MemberService memberService,
            TaskService taskService,
            CommentService commentService) {
        return new PerformanceKeeperFacade(userService, courseService, memberService, taskService, commentService);
    }
    @Bean
    public TimeTracer TimeTracer() {
        return new TimeTracer();
    }
}
