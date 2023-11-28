package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseService;
import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.member.MemberServiceImpl;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final CourseServiceImpl courseServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    public void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity) {
        List<TaskEntity> existingTasks = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
        for (TaskEntity existingTask : existingTasks) {
            TaskEntity newTaskEntity = TaskEntity.fromEntity(existingTask);
            newTaskEntity.setMember(memberEntity);
            taskRepository.save(newTaskEntity);
        }
    }
    public void createTask(Long userId, Long courseId, TaskCreateDto taskCreateDto) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        memberServiceImpl.checkManagerMember(user, course);
        List<MemberEntity> studentsOfThisCourse = memberServiceImpl.getAllStudentsOfThisCourse(course);
        for (MemberEntity studentMemberEntity : studentsOfThisCourse) {
            TaskEntity taskEntity = TaskCreateDto.toEntity(taskCreateDto);
            taskEntity.setMember(studentMemberEntity);
            taskEntity.setCourse(course);
            taskEntity.setStatus("등록");
            taskRepository.save(taskEntity);
        }
    }
}
