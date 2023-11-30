package com.example.performancekeeper.frontend;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views/manager/")
public class ManagerViewController {

    @GetMapping("/login")
    public String managerLoginPage() {
        return "/manager/login-page";
    }

    @GetMapping("/main")
    public String managerMainPage() {
        return "/manager/index";
    }

    @GetMapping("/course-create-page")
    public String courseCreatePage() {
        return "/manager/course-create-page";
    }
}
