package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.task.*;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import com.example.performancekeeper.api.repository.AssignedTaskRepository;
import com.example.performancekeeper.api.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final AssignedTaskRepository assignedTaskRepository;

    public void assignTasksToNewStudent(CourseEntity course, MemberEntity member) { // 새 학생에게 기존의 과제들을 부여하기
        List<TaskEntity> existingTasks = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
        List<AssignedTaskEntity> assignedTaskEntitiesToNewStudent = new ArrayList<>();

        for (TaskEntity existingTask : existingTasks) {
            AssignedTaskEntity newTaskEntity = AssignedTaskEntity.fromTaskEntity(existingTask, member);
            assignedTaskEntitiesToNewStudent.add(newTaskEntity);
        }
        assignedTaskRepository.saveAll(assignedTaskEntitiesToNewStudent);
    }

    @Transactional
    public void createTask(CourseEntity course, TaskCreateDto taskCreateDto, List<MemberEntity> studentsOfThisCourse) {
        TaskEntity taskEntity = TaskCreateDto.toEntity(taskCreateDto, course);
        taskEntity = taskRepository.save(taskEntity);

        for (MemberEntity studentMemberEntity : studentsOfThisCourse) { // 기존 학생들에게 부여
            AssignedTaskEntity assignedTaskEntity = AssignedTaskEntity.fromTaskEntity(taskEntity, studentMemberEntity);
            assignedTaskRepository.save(assignedTaskEntity);
        }
    }

    public int getProgress(MemberEntity member) {
        if (member.getRole().equals("Student")) {
            List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndDeletedAtIsNull(member);
            int completed = 0;
            for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityList)
                if (assignedTaskEntity.getStatus().equals("완료")) completed++;
            int all = assignedTaskEntityList.size();
            return all == 0 ? 0 : completed * 100 / all;
        } else {
            CourseEntity course = member.getCourse();
            int completed = 0;
            List<TaskEntity> taskEntityList = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
            int all = 0;
            for (TaskEntity task : taskEntityList) {
                List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllAssignedTaskEntitiesByTaskAndDeletedAtIsNull(task);
                all += assignedTaskEntityList.size();
                completed += countCompletedAssignedTask(assignedTaskEntityList);
            }

            return all == 0 ? 0 : completed * 100 / all;
        }
    }

    private int countCompletedAssignedTask(List<AssignedTaskEntity> assignedTaskEntityList) {
        int result = 0;
        for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityList) if (assignedTaskEntity.getStatus().equals("완료")) result++;
        return result;
    }

    @Override
    public List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByKeyword(MemberEntity member, String keyword) {
        List<TaskEntity> taskEntityList = taskRepository.findAllByCourseAndNameContainingAndDeletedAtIsNull(member.getCourse(), keyword);
        List<AssignedTaskOverviewDto>[] result = new ArrayList[] {new ArrayList(), new ArrayList()};
        for (TaskEntity task : taskEntityList) {
            AssignedTaskEntity assignedTask = assignedTaskRepository.findByMemberAndTaskAndDeletedAtIsNull(member, task);
            if (assignedTask.getStatus().equals("완료")) result[0].add(AssignedTaskOverviewDto.fromEntity(assignedTask));
            else result[1].add(AssignedTaskOverviewDto.fromEntity(assignedTask));
        }
        return result;
    }

    public List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByDate(MemberEntity member, LocalDate date) { // 학생이 날짜별 자신의 진행상황 조회
        List<TaskEntity> taskList = taskRepository.findAllByCourseAndStartAtAndDeletedAtIsNull(member.getCourse(), date);
        List<AssignedTaskEntity> assignedTaskEntityList = new ArrayList<>();
        for (TaskEntity task : taskList)
            assignedTaskEntityList.add(assignedTaskRepository.findByMemberAndTaskAndDeletedAtIsNull(member, task));
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

    public void updateTaskStatus(AssignedTaskEntity assignedTask, TaskStatusDto taskStatusDto) {
        assignedTask.setStatus(taskStatusDto.getSelectedStatus());
        assignedTaskRepository.save(assignedTask);
    }

    public List<Object> getTasksByDate(CourseEntity course, LocalDate startAt) {

        List<Object> result = new ArrayList<>();
        List<TaskEntity> taskEntityList =
                taskRepository.findAllByCourseAndStartAtAndDeletedAtIsNull(course, startAt);
        taskEntityList.sort(Comparator.comparing(TaskEntity::getCreatedAt).thenComparing(TaskEntity::getId)); // 생성순 정렬
        for (TaskEntity taskEntity : taskEntityList) result.add(TaskOverviewDto.fromEntity(taskEntity));
        return result;
    }

    public Map<MemberOverviewDto, List<AssignedTaskStatusDto>> getProgressesByDate(List<MemberEntity> studentsOfThisCourse, LocalDate startAt) {
        Map<MemberOverviewDto, List<AssignedTaskStatusDto>> result = new HashMap<>();
        for (MemberEntity student : studentsOfThisCourse) {
            MemberOverviewDto key = MemberOverviewDto.fromEntity(student);
            List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndDeletedAtIsNull(student); // startAt도 넣어서 찾으면 좋은지 확인하기
            List<AssignedTaskStatusDto> value = new ArrayList<>();
            for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityList)
                if (assignedTaskEntity.getTask().getStartAt().equals(startAt)) value.add(AssignedTaskStatusDto.fromEntity(assignedTaskEntity));
            result.put(key,value);
        }
        return result;
    }

    public Map<String, Object> getTasksAndProgressesByDate(CourseEntity course, List<MemberEntity> studentsOfThisCourse, LocalDate startAt) {
        Map<String, Object> result = new HashMap<>();
        result.put("taskList", getTasksByDate(course, startAt));
        result.put("progresses", getProgressesByDate(studentsOfThisCourse, startAt));
        return result;
    }

    public void deleteAssignedTasksOfLeavingStudent(MemberEntity member) {
        LocalDateTime now = LocalDateTime.now();
        List<AssignedTaskEntity> assignedTaskEntityListToBeDeleted = assignedTaskRepository.findAllByMemberAndDeletedAtIsNull(member);
        for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityListToBeDeleted) {
            assignedTaskEntity.setDeletedAt(now);
            assignedTaskRepository.save(assignedTaskEntity);
        }
    }

    public int[] getProgressOfThisTask(TaskEntity task) {
        List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByTaskAndDeletedAtIsNull(task);
        int[] result = new int[4]; //   index 0: 완료   1: 진행중   2: 에러   3: 등록
        for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityList) {
            String status = assignedTaskEntity.getStatus();
            switch (status) {
                case "완료" -> result[0]++;
                case "진행중" -> result[1]++;
                case "에러" -> result[2]++;
                case "등록" -> result[3]++;
            }
        }
        return result;
    }

    @Override
    public AssignedTaskEntity checkAssignedTask(Long assignedTaskId) {
        return assignedTaskRepository.findByIdAndDeletedAtIsNull(assignedTaskId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
    }

    @Override
    public TaskOverviewDto getTaskDetails(AssignedTaskEntity assignedTask) {
        return TaskOverviewDto.fromEntity(assignedTask.getTask());
    }

    @Override
    public TaskEntity checkTask(Long taskId) {
        return taskRepository.findByIdAndDeletedAtIsNull(taskId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
    }

    @Override
    public List<AssignedTaskOverviewDto> getUncompletedAssignedTasksListOfThisCourse(MemberEntity member) {
        List<AssignedTaskOverviewDto> result = new ArrayList<>();
        List<AssignedTaskEntity> theUncompleted = assignedTaskRepository.findAllByMemberAndStatusIsNotAndDeletedAtIsNull(member, "완료");
        for (AssignedTaskEntity uncompleted : theUncompleted) result.add(AssignedTaskOverviewDto.fromEntity(uncompleted));
        return result;
    }
}
