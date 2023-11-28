package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final MemberService memberService;
    private final UserService userService;
    public void createCourse(Long userId, CourseCreateDto courseCreateDto) {
        UserEntity userEntity = userService.checkUserEntity(userId);
        CourseEntity courseEntity = CourseCreateDto.toEntity(courseCreateDto);
        courseEntity = courseRepository.save(courseEntity);
        memberService.createManager(userEntity, courseEntity);
    }
}
