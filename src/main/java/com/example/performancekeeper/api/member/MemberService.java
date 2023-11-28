package com.example.performancekeeper.api.member;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseService;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TaskService taskService;

    public void createManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.save(new MemberEntity(user, course, "Manager"));
    }
    public void createStudentMember(Long userId, Long courseId, JoinCourseDto joinCourseDto) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseService.checkCourseEntity(courseId);
        if (!course.getJoinCode().equals(passwordEncoder.encode(joinCourseDto.getJoinCode())))
            throw new CustomException(CustomErrorCode.JOINCODE_MISMATCH);
        MemberEntity memberEntity = memberRepository.save(new MemberEntity(user, course, "Student"));
        taskService.assignTasksToNewStudent(course, memberEntity); // 새 학생에게 기존의 과제물 부여
    }
}
