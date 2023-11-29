package com.example.performancekeeper.api.course;

import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
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
    private final UserService userService;
    private final TaskService taskService;

    @Transactional
    public void createCourse(Long userId, CourseCreateDto courseCreateDto) {
        UserEntity userEntity = userService.checkUserEntity(userId);
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
        UserEntity user = userService.checkUserEntity(userId);
        List<MemberEntity> myMembers = memberService.getMyMember(user);
        List<MyCourseOverviewDto> myCoursesDtoList = new ArrayList<>();
        for (MemberEntity memberEntity : myMembers) {
            CourseEntity course = memberEntity.getCourse();
            MyCourseOverviewDto myCourseOverviewDto = new MyCourseOverviewDto(course.getId(), course.getName(), memberEntity.getRole());
            if (memberEntity.getRole().equals("Student"))
                myCourseOverviewDto.setProgress(taskService.getProgress(memberEntity));
            myCoursesDtoList.add(myCourseOverviewDto);
        }
        return myCoursesDtoList;
    }
}
