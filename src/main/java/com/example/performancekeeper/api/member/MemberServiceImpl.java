package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final UserServiceImpl userServiceImpl;
    private final CourseServiceImpl courseServiceImpl;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TaskService taskService;

    public void checkManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Manager")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_MANAGER));
    }
    public List<MemberEntity> getAllStudentsOfThisCourse(CourseEntity course) {
        return memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(course, "Student");
    }

    public MemberEntity checkStudentMember(UserEntity user, CourseEntity course) {
        return memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_STUDENT));
    }

    public MemberEntity checkMember(UserEntity user, CourseEntity course) {
        return memberRepository.findByUserAndCourseAndDeletedAtIsNull(user, course)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_MEMBER));
    }

    public void createManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.save(new MemberEntity(user, course, "Manager"));
    }

    public MemberEntity createStudentMember(UserEntity user, CourseEntity course, JoinCourseDto joinCourseDto) {
        if (!passwordEncoder.matches(joinCourseDto.getJoinCode(), course.getJoinCode()))
            throw new CustomException(CustomErrorCode.JOINCODE_MISMATCH); // 참여코드 일치 확인
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student");
        if (optionalMemberEntity.isPresent()) throw new CustomException(CustomErrorCode.ALREADY_JOIN);
        return memberRepository.save(new MemberEntity(user, course, "Student"));
    }

    public List<MemberEntity> getMyMember(UserEntity user) {
        return memberRepository.findAllByUserAndDeletedAtIsNull(user);
    }
    @Transactional
    public void deleteStudentMember(Long userId, Long courseId, LeaveRequestDto leaveRequestDto) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_STUDENT));
        if (!course.getName().equals(leaveRequestDto.getCourseNameCheck())) throw new CustomException(CustomErrorCode.WRONG_COURSE_NAME);

        taskService.deleteAssignedTasksOfLeavingStudent(member); // 나가려는 유저에게 부여된 과제들 삭제처리
        member.setDeletedAt(LocalDateTime.now());
        memberRepository.save(member); // soft deletion

    }

    public void changeNickname(Long userId, Long courseId, NicknameUpdateDto nicknameUpdateDto) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_STUDENT));
        member.setNickname(nicknameUpdateDto.getNickname());
        memberRepository.save(member);
    }

    public MemberOverviewDto getMemberInfo(Long userId, Long courseId) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberRepository.findByUserAndCourseAndDeletedAtIsNull(user, course)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_MEMBER));
        return MemberOverviewDto.fromEntity(member);
    }
}
