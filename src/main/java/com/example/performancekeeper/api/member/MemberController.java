package com.example.performancekeeper.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/member")
public class MemberController {
    private final MemberService memberService;
    @PostMapping
    public void createStudentMember(Authentication authentication,
                                    @PathVariable("courseId") Long courseId,
                                    @RequestBody JoinCourseDto joinCourseDto) {
        Long userId = Long.parseLong(authentication.getName());
        memberService.createStudentMember(userId, courseId, joinCourseDto);
    }
}
