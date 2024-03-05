package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.facade.MemberControllerFacade;
import com.example.performancekeeper.api.dto.member.JoinCourseDto;
import com.example.performancekeeper.api.dto.member.LeaveRequestDto;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.dto.member.NicknameUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 강의실에 입실한 멤버 관련 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/member")
public class MemberController {
    private final MemberControllerFacade performanceKeeperFacade;

    /**
     * 학생 멤버 등록 요청을 처리하는 메서드
     *
     * @param authentication
     * @param courseId
     * @param joinCourseDto 강의실 입실 코드를 담은 Dto 객체
     */
    @PostMapping// 강의실 입실
    public void createStudentMember(Authentication authentication,
                                    @PathVariable("courseId") Long courseId,
                                    @RequestBody JoinCourseDto joinCourseDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createStudentMember(userId, courseId, joinCourseDto);
    }

    /**
     * 학생 멤버 삭제 요청을 처리하는 메서드
     *
     * @param authentication
     * @param courseId
     * @param leaveRequestDto 삭제 의사를 담은 Dto 객체
     */
    @DeleteMapping// 강의실 퇴실
    public void leaveCourse(Authentication authentication,
                            @PathVariable("courseId") Long courseId,
                            @RequestBody LeaveRequestDto leaveRequestDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.deleteStudentMember(userId, courseId, leaveRequestDto);
    }

    /**
     * 멤버의 별명 수정 요청을 처리하는 메서드
     *
     * @param authentication
     * @param courseId
     * @param nicknameUpdateDto 새로운 별명을 담은 Dto 객체
     */
    @PutMapping
    public void changeNickname(Authentication authentication,
                               @PathVariable("courseId") Long courseId,
                               @RequestBody NicknameUpdateDto nicknameUpdateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.changeNickname(userId, courseId, nicknameUpdateDto);
    }

    /**
     * 멤버 정보 조회 요청을 처리하는 메서드
     *
     * @param authentication
     * @param courseId
     * @return 멤버의 대략적인 정보를 담은 Dto 객체 반환
     */
    @GetMapping
    public MemberOverviewDto getMemberInfo(Authentication authentication,
                                           @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getMemberInfo(userId, courseId);
    }
}
