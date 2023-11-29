package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;

import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final CourseServiceImpl courseServiceImpl;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TaskService taskService;

    public void createManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.save(new MemberEntity(user, course, "Manager"));
    }
    @Transactional
    public void createStudentMember(Long userId, Long courseId, JoinCourseDto joinCourseDto) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        if (!passwordEncoder.matches(joinCourseDto.getJoinCode(), course.getJoinCode()))
            throw new CustomException(CustomErrorCode.JOINCODE_MISMATCH);
        MemberEntity memberEntity = memberRepository.save(new MemberEntity(user, course, "Student"));
        taskService.assignTasksToNewStudent(course, memberEntity); // 새 학생에게 기존의 과제물 부여
    }

    public List<MemberEntity> getMyMember(UserEntity user) {
        return memberRepository.findAllByUserAndDeletedAtIsNull(user);
    }
}
