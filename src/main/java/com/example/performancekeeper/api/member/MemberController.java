package com.example.performancekeeper.api.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/member")
public class MemberController {
    private final MemberService memberService;
    @PostMapping// 강의실 입실
    public void createStudentMember(Authentication authentication,
                                    @PathVariable("courseId") Long courseId,
                                    @RequestBody JoinCourseDto joinCourseDto) {
        Long userId = Long.parseLong(authentication.getName());
        memberService.createStudentMember(userId, courseId, joinCourseDto);
    }

    @DeleteMapping// 강의실 퇴실
    public void leaveCourse(Authentication authentication,
                            @PathVariable("courseId") Long courseId,
                            @RequestBody LeaveRequestDto leaveRequestDto) {
        Long userId = Long.parseLong(authentication.getName());
        memberService.deleteStudentMember(userId, courseId, leaveRequestDto);
    }

    @PutMapping
    public void changeNickname(Authentication authentication,
                               @PathVariable("courseId") Long courseId,
                               @RequestBody NicknameUpdateDto nicknameUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        memberService.changeNickname(userId, courseId, nicknameUpdateDto);
    }
}
