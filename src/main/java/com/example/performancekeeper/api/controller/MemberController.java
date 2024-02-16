package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.common.PerformanceKeeperFacade;
import com.example.performancekeeper.api.dto.member.JoinCourseDto;
import com.example.performancekeeper.api.dto.member.LeaveRequestDto;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.dto.member.NicknameUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/member")
public class MemberController {
    private final PerformanceKeeperFacade performanceKeeperFacade;
    @PostMapping// 강의실 입실
    public void createStudentMember(Authentication authentication,
                                    @PathVariable("courseId") Long courseId,
                                    @RequestBody JoinCourseDto joinCourseDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createStudentMember(userId, courseId, joinCourseDto);
    }

    @DeleteMapping// 강의실 퇴실
    public void leaveCourse(Authentication authentication,
                            @PathVariable("courseId") Long courseId,
                            @RequestBody LeaveRequestDto leaveRequestDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.deleteStudentMember(userId, courseId, leaveRequestDto);
    }

    @PutMapping
    public void changeNickname(Authentication authentication,
                               @PathVariable("courseId") Long courseId,
                               @RequestBody NicknameUpdateDto nicknameUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.changeNickname(userId, courseId, nicknameUpdateDto);
    }

    @GetMapping
    public MemberOverviewDto getMemberInfo(Authentication authentication,
                                           @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getMemberInfo(userId, courseId);
    }
}
