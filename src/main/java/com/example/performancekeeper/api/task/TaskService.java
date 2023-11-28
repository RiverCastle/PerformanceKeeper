package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity) {
        List<TaskEntity> existingTasks = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
        for (TaskEntity existingTask : existingTasks) {
            TaskEntity newTaskEntity = TaskEntity.fromEntity(existingTask);
            newTaskEntity.setMember(memberEntity);
            taskRepository.save(newTaskEntity);
        }
    }
}
