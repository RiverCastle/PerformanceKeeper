package com.example.performancekeeper.frontend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/views")
@RequiredArgsConstructor
public class ViewController {
    @GetMapping("/sign-up")
    public String signUpPage() {
        log.info("to sign up page");
        return "sign-up-page";
    }

    @GetMapping("/login")
    public String logInPage() {
        log.info("to login page");
        return "login-page";
    }

    @GetMapping("/main")
    public String mainPage() {
        log.info("to main page");
        return "service-main";
    }

    @GetMapping("/course-create-page")
    public String courseCreatePage(Authentication authentication) {
        log.info("to Course Create Page");
        return "course-create-page";
    }

    @GetMapping("/course-list-page")
    public String courseListPage() {
        log.info("to course-list-page");
        return "course-list-page";
    }
}
