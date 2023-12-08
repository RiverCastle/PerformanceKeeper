package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberOverviewDto;
import com.example.performancekeeper.api.member.MemberServiceImpl;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserServiceImpl;
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
    private final UserServiceImpl userServiceImpl;
    private final CourseServiceImpl courseServiceImpl;
    private final MemberServiceImpl memberServiceImpl;

    public void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity) { // 새 학생에게 기존의 과제들을 부여하기
        List<TaskEntity> existingTasks = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
        List<AssignedTaskEntity> assignedTaskEntitiesToNewStudent = new ArrayList<>();
        for (TaskEntity existingTask : existingTasks) {
            AssignedTaskEntity newTaskEntity = AssignedTaskEntity.fromTaskEntity(existingTask);
            newTaskEntity.setMember(memberEntity);
            assignedTaskEntitiesToNewStudent.add(newTaskEntity);
        }
        assignedTaskRepository.saveAll(assignedTaskEntitiesToNewStudent);
    }
    @Transactional
    public void createTask(Long userId, Long courseId, TaskCreateDto taskCreateDto) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
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
            for (TaskEntity task : taskEntityList) completed += assignedTaskRepository.countAssignedTaskEntitiesByTaskAndStatusAndDeletedAtIsNull(task, "완료");
            int all = memberServiceImpl.getAllStudentsOfThisCourse(course).size() * taskEntityList.size();
            return all == 0? 0 : completed * 100 / all;
        }
    }

//    public List<AssignedTaskOverviewDto>[] searchTasksByKeyword(Long userId, Long courseId, String keyword) {
//        UserEntity user = userServiceImpl.checkUserEntity(userId);
//        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
//        MemberEntity member = memberServiceImpl.checkStudentMember(user, course);
//        List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndNameContainingAndDeletedAtIsNull(member, keyword);
//        return sortByStatus(assignedTaskEntityList);
//    }

    public List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByDate(Long userId, Long courseId, LocalDate date) { // 학생이 날짜별 자신의 진행상황 조회
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkStudentMember(user, course);
        List<TaskEntity> taskList = taskRepository.findAllByCourseAndStartAtAndDeletedAtIsNull(course, date);
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

    public void updateTaskStatus(Long userId, Long courseId, Long taskId, TaskStatusDto taskStatusDto) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkStudentMember(user, course);
        AssignedTaskEntity assignedTask = assignedTaskRepository.findById(taskId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
        if (!assignedTask.getMember().equals(member)) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        assignedTask.setStatus(taskStatusDto.getSelectedStatus());
        assignedTaskRepository.save(assignedTask);
    }

    public List<Object> getTasksByDate(Long userId, Long courseId, LocalDate startAt) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        memberServiceImpl.checkManagerMember(user, course);

        List<Object> result = new ArrayList<>();
        List<TaskEntity> taskEntityList =
                taskRepository.findAllByCourseAndStartAtAndDeletedAtIsNull(course, startAt);
        taskEntityList.sort(Comparator.comparing(TaskEntity::getCreatedAt).thenComparing(TaskEntity::getId)); // 생성순 정렬
        for (TaskEntity taskEntity : taskEntityList) result.add(TaskOverviewDto.fromEntity(taskEntity));
        return result;
    }

    public Map<MemberOverviewDto, List<AssignedTaskStatusDto>> getProgressesByDate(Long userId, Long courseId, LocalDate startAt) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        memberServiceImpl.checkManagerMember(user, course);
        // ↑ 3줄 필요X

        Map<MemberOverviewDto, List<AssignedTaskStatusDto>> result = new HashMap<>();
        List<MemberEntity> studentsOfThisCourse = memberServiceImpl.getAllStudentsOfThisCourse(course);
        for (MemberEntity student : studentsOfThisCourse) {
            MemberOverviewDto key = MemberOverviewDto.fromEntity(student);
            List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByMemberAndDeletedAtIsNull(student);
            List<AssignedTaskStatusDto> value = new ArrayList<>();
            for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityList)
                if (assignedTaskEntity.getTask().getStartAt().equals(startAt)) value.add(AssignedTaskStatusDto.fromEntity(assignedTaskEntity));
            result.put(key,value);
        }
        return result;
    }

    public Map<String, Object> getTasksAndProgressesByDate(Long userId, Long courseId, LocalDate startAt) {
        Map<String, Object> result = new HashMap<>();
        result.put("taskList", getTasksByDate(userId, courseId, startAt));
        result.put("progresses", getProgressesByDate(userId, courseId, startAt));
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

    public int[] getProgressOfThisTask(Long userId, Long courseId, Long taskId) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        memberServiceImpl.checkManagerMember(user, course);
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
        List<AssignedTaskEntity> assignedTaskEntityList = assignedTaskRepository.findAllByTaskAndDeletedAtIsNull(task);
        int[] result = new int[4]; // index 0: 완료 1: 진행중 2: 에러 3: 등록
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
}
