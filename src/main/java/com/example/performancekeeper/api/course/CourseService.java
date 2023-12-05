package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserServiceImpl;
import jakarta.transaction.Transactional;
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
    private final UserServiceImpl userServiceImpl;
    private final TaskService taskService;

    @Transactional
    public void createCourse(Long userId, CourseCreateDto courseCreateDto) {
        UserEntity userEntity = userServiceImpl.checkUserEntity(userId);
        courseCreateDto.setJoinCode(passwordEncoder.encode(courseCreateDto.getJoinCode()));
        CourseEntity courseEntity = CourseCreateDto.toEntity(courseCreateDto);
        courseEntity = courseRepository.save(courseEntity);
        memberService.createManagerMember(userEntity, courseEntity);
    }

    public List<CourseOverviewDto> searchCourse(String keyword) {
        List<CourseEntity> courseEntities = courseRepository.findAllByNameContainingAndDeletedAtIsNull(keyword);
        List<CourseOverviewDto> result = new ArrayList<>();
        for (CourseEntity entity : courseEntities) result.add(CourseOverviewDto.fromEntity(entity));
        return result;
    }

    public List<MyCourseOverviewDto> getMyCourses(Long userId) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        List<MemberEntity> myMembers = memberService.getMyMember(user);
        List<MyCourseOverviewDto> myCoursesDtoList = new ArrayList<>();
        for (MemberEntity memberEntity : myMembers) {
            CourseEntity course = memberEntity.getCourse();
            MyCourseOverviewDto myCourseOverviewDto = new MyCourseOverviewDto(course.getId(), course.getName(), memberEntity.getRole());
            myCourseOverviewDto.setProgress(taskService.getProgress(memberEntity));
            myCoursesDtoList.add(myCourseOverviewDto);
        }
        return myCoursesDtoList;
    }

    public CourseDetailsDto getCourseDetails(Long userId, Long courseId) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
        List<MemberEntity> myMembers = memberService.getMyMember(user);
        for (MemberEntity member : myMembers) {
            CourseEntity targetCourse = member.getCourse();
            if (targetCourse.equals(course)) return CourseDetailsDto.fromEntity(targetCourse);
        }
        throw new CustomException(CustomErrorCode.NOT_MEMBER);
    }

    public void updateCourseName(Long userId, Long courseId, CourseNameUpdateDto courseNameUpdateDto) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
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
        UserEntity user = userServiceImpl.checkUserEntity(userId);
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
}
