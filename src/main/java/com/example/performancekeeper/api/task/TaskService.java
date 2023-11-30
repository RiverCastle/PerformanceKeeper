package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberServiceImpl;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final AssignedTaskRepository assignedTaskRepository;
    private final UserService userService;
    private final CourseServiceImpl courseServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    public void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity) {
        List<TaskEntity> existingTasks = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
        for (TaskEntity existingTask : existingTasks) {
            AssignedTaskEntity newTaskEntity = AssignedTaskEntity.fromTaskEntity(existingTask);
            newTaskEntity.setMember(memberEntity);
            assignedTaskRepository.save(newTaskEntity);
        }
    }
    @Transactional
    public void createTask(Long userId, Long courseId, TaskCreateDto taskCreateDto) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        memberServiceImpl.checkManagerMember(user, course);
        TaskEntity taskEntity = TaskCreateDto.toEntity(taskCreateDto);
        taskEntity.setCourse(course);
        taskEntity = taskRepository.save(taskEntity);

        List<MemberEntity> studentsOfThisCourse = memberServiceImpl.getAllStudentsOfThisCourse(course); // 기존 학생들에게 부여
        for (MemberEntity studentMemberEntity : studentsOfThisCourse) {
            AssignedTaskEntity assignedTaskEntity = AssignedTaskEntity.fromTaskEntity(taskEntity);
            assignedTaskEntity.setMember(studentMemberEntity);
            assignedTaskRepository.save(assignedTaskEntity);
        }
    }

    public int getProgress(MemberEntity member) {
        List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndDeletedAtIsNull(member);
        int completed = 0;
        for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityList) if (assignedTaskEntity.getStatus().equals("완료")) completed++;
        int all = assignedTaskEntityList.size();
        return all == 0 ? 0 : completed * 100 / all;
    }

    public List<AssignedTaskOverviewDto>[] searchTasksByKeyword(Long userId, Long courseId, String keyword) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkStudentMember(user, course);
        List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndNameContainingAndDeletedAtIsNull(member, keyword);
        return sortByStatus(assignedTaskEntityList);
    }

    public List<AssignedTaskOverviewDto>[] searchTasksByDate(Long userId, Long courseId, LocalDate date) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkStudentMember(user, course);
        List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndStartAtAndDeletedAtIsNull(member, date);
        return sortByStatus(assignedTaskEntityList);
    }

    private List<AssignedTaskOverviewDto>[] sortByStatus(List<AssignedTaskEntity> entityList) {
        List<AssignedTaskOverviewDto>[] result = new ArrayList[] {new ArrayList(), new ArrayList()};

        for (AssignedTaskEntity entity : entityList) {
            if (entity.getStatus().equals("완료")) result[0].add(AssignedTaskOverviewDto.fromEntity(entity));
            else result[1].add(AssignedTaskOverviewDto.fromEntity(entity));
        }
        return result;
    }

    public void updateTaskStatus(Long userId, Long courseId, Long taskId, TaskStatusDto taskStatusDto) {
        UserEntity user = userService.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkStudentMember(user, course);
        AssignedTaskEntity assignedTask = assignedTaskRepository.findById(taskId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
        if (!assignedTask.getMember().equals(member)) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        assignedTask.setStatus(taskStatusDto.getSelectedStatus());
        assignedTaskRepository.save(assignedTask);
    }
}
