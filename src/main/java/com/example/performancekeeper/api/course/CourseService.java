package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CourseService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final MemberService memberService;
    private final UserService userService;
    public void createCourse(Long userId, CourseCreateDto courseCreateDto) {
        UserEntity userEntity = userService.checkUserEntity(userId);
        courseCreateDto.setJoinCode(passwordEncoder.encode(courseCreateDto.getJoinCode()));
        CourseEntity courseEntity = CourseCreateDto.toEntity(courseCreateDto);
        courseEntity = courseRepository.save(courseEntity);
        memberService.createManager(userEntity, courseEntity);
    }

    public List<CourseOverviewDto> searchCourse(String keyword) {
        List<CourseEntity> courseEntities = courseRepository.findAllByNameContainingAndDeletedAtIsNull(keyword);
        List<CourseOverviewDto> result = new ArrayList<>();
        for (CourseEntity entity : courseEntities) result.add(CourseOverviewDto.fromEntity(entity));
        return result;
    }

    public CourseEntity checkCourseEntity(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }
}
