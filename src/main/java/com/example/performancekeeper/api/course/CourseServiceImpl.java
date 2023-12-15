package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final MemberService memberService;
    private final UserServiceImpl userServiceImpl;
    private final TaskService taskService;

    public CourseEntity createCourse(CourseCreateDto courseCreateDto) {
        courseCreateDto.setJoinCode(passwordEncoder.encode(courseCreateDto.getJoinCode())); // 가입코드 암호화
        return courseRepository.save(CourseCreateDto.toEntity(courseCreateDto));
    }

    public List<CourseOverviewDto> searchCourse(String keyword) {
        List<CourseEntity> courseEntities = courseRepository.findAllByNameContainingAndDeletedAtIsNull(keyword);
        List<CourseOverviewDto> result = new ArrayList<>();
        for (CourseEntity entity : courseEntities) result.add(CourseOverviewDto.fromEntity(entity));
        return result;
    }

    public CourseDetailsDto getCourseDetails(Long userId, Long courseId) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
        List<MemberEntity> myMembers = memberService.getMyMember(user);
        for (MemberEntity member : myMembers) {
            CourseEntity targetCourse = member.getCourse();
            if (targetCourse.equals(course)) return CourseDetailsDto.fromEntity(targetCourse);
        }
        throw new CustomException(CustomErrorCode.NOT_MEMBER);
    }

    public void updateCourseName(Long userId, Long courseId, CourseNameUpdateDto courseNameUpdateDto) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
        List<MemberEntity> myMembers = memberService.getMyMember(user);
        for (MemberEntity member : myMembers) {
            CourseEntity targetCourse = member.getCourse();
            if (targetCourse.equals(course) && member.getRole().equals("Manager")) {
                course.setName(courseNameUpdateDto.getNewName());
                courseRepository.save(course);
                return;
            }
        }
        throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
    }

    public void updateDescriptionName(Long userId, Long courseId, CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
        List<MemberEntity> myMembers = memberService.getMyMember(user);
        for (MemberEntity member : myMembers) {
            CourseEntity targetCourse = member.getCourse();
            if (targetCourse.equals(course) && member.getRole().equals("Manager")) {
                course.setDescription(courseDescriptionUpdateDto.getNewDescription());
                courseRepository.save(course);
                return;
            }
        }
        throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
    }

    public CourseEntity checkCourseEntity(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }

    @Override
    public CourseEntity checkCourse(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }
}
