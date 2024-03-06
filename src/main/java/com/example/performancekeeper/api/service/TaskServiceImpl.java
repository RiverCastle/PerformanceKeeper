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

/**
 * 과제 관련 비즈니스 로직을 처리하는 인터페이스 TaskService의 구현체 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final AssignedTaskRepository assignedTaskRepository;

    /**
     * 새로 가입한 학생에게 기존의 과제들을 부여하는 메서드입니다.
     * 해당 강의실에 존재하는 과제들을 조회한 후,
     * 새 학생에 대한 AssignedTaskEntity 객체들을 생성 및 저장합니다.
     *
     * @param course 해당 과목 엔티티
     * @param member 새로운 학생 엔티티
     */
    public void assignTasksToNewStudent(CourseEntity course, MemberEntity member) { // 새 학생에게 기존의 과제들을 부여하기
        List<TaskEntity> existingTasks = taskRepository.findAllByCourseAndDeletedAtIsNull(course);
        List<AssignedTaskEntity> assignedTaskEntitiesToNewStudent = new ArrayList<>();

        for (TaskEntity existingTask : existingTasks) {
            AssignedTaskEntity newTaskEntity = AssignedTaskEntity.fromTaskEntity(existingTask, member);
            assignedTaskEntitiesToNewStudent.add(newTaskEntity);
        }
        assignedTaskRepository.saveAll(assignedTaskEntitiesToNewStudent);
    }

    /**
     * 새로운 과제를 생성하고, 해당 과제를 해당 과목의 모든 학생에게 부여하는 메서드입니다.
     * 강의실 매니저 사용자에 의해 입력된 데이터를 바탕으로 TaskEntity 객체를 생성 및 저장한 후,
     * 강의실 학생 사용자에 대하여 AssignedTaskEntity 객체들을 생성 및 저장합니다.
     *
     * @param course               해당 과목 엔티티
     * @param taskCreateDto        새로운 과제 생성을 위한 DTO 객체
     * @param studentsOfThisCourse 해당 과목의 모든 학생 엔티티 목록
     */
    @Transactional
    public void createTask(CourseEntity course, TaskCreateDto taskCreateDto, List<MemberEntity> studentsOfThisCourse) {
        TaskEntity taskEntity = TaskCreateDto.toEntity(taskCreateDto, course);
        taskEntity = taskRepository.save(taskEntity);

        for (MemberEntity studentMemberEntity : studentsOfThisCourse) { // 기존 학생들에게 부여
            AssignedTaskEntity assignedTaskEntity = AssignedTaskEntity.fromTaskEntity(taskEntity, studentMemberEntity);
            assignedTaskRepository.save(assignedTaskEntity);
        }
    }

    /**
     * 학생의 과제 완수율을 파악하는 메서드입니다.
     * 학생 사용자의 경우, 자신이 부여받은 전체 과제 중 완료된 과제의 수에 대하여 완수율을 조회할 수 있습니다.
     * 강의실 매니저 사용자의 경우, 자신이 담당하고 있는 강의실에 부여된 전체 과제 중 완료된 과제의 수에 대하여 완수율을 조회할 수 있습니다.
     *
     * @param member 진행률을 조회할 학생 또는 강사 엔티티
     * @return 완수율
     */
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

    /**
     * 완료된 과제와 미완료된 과제를 키워드로 검색하는 메서드입니다.
     *
     * @param member  검색을 수행할 회원 엔티티
     * @param keyword 검색할 키워드
     * @return 완료된 과제와 미완료된 과제를 포함하는 배열
     */
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

    /**
     * 학생의 특정 날짜별 진행 상황을 조회하는 메서드입니다.
     *
     * @param member 학생 엔티티
     * @param date   조회할 날짜
     * @return 완료된 과제와 미완료된 과제를 포함하는 배열
     */
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

    /**
     * 과제 진행상황을 변경하는 메서드입니다.
     *
     * @param assignedTask 부여된 과제 엔티티
     * @param taskStatusDto 업데이트할 과제 상태 정보를 담은 DTO 객체
     */
    public void updateTaskStatus(AssignedTaskEntity assignedTask, TaskStatusDto taskStatusDto) {
        assignedTask.setStatus(taskStatusDto.getSelectedStatus());
        assignedTaskRepository.save(assignedTask);
    }

    /**
     * 특정 날짜에 해당하는 과제 목록을 조회합니다.
     * 생성된 순으로 조회된 과제 목록을 정렬합니다.
     *
     * @param course  과목 엔티티
     * @param startAt 시작 날짜
     * @return 특정 날짜에 해당하는 과제 목록
     */
    public List<Object> getTasksByDate(CourseEntity course, LocalDate startAt) {

        List<Object> result = new ArrayList<>();
        List<TaskEntity> taskEntityList =
                taskRepository.findAllByCourseAndStartAtAndDeletedAtIsNull(course, startAt);
        taskEntityList.sort(Comparator.comparing(TaskEntity::getCreatedAt).thenComparing(TaskEntity::getId)); // 생성순 정렬
        for (TaskEntity taskEntity : taskEntityList) result.add(TaskOverviewDto.fromEntity(taskEntity));
        return result;
    }

    /**
     * 특정 날짜에 해당하는 학생들의 진행 상황을 조회합니다.
     * 멤버 정보를 Key, 과제별 상태 목록을 Value로 갖는 Map에 데이터를 담습니다.
     *
     * @param studentsOfThisCourse 해당 과목의 학생 엔티티 목록
     * @param startAt               시작 날짜
     * @return 특정 날짜에 해당하는 학생들의 진행 상황
     */
    public Map<MemberOverviewDto, List<AssignedTaskStatusDto>> getProgressesByDate(List<MemberEntity> studentsOfThisCourse, LocalDate startAt) {
        Map<MemberOverviewDto, List<AssignedTaskStatusDto>> result = new HashMap<>();
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

    /**
     * 특정 날짜에 해당하는 과제 목록과 학생들의 진행 상황을 조회합니다.
     * 입력된 날짜에 해당하는 과제 목록을 taskList 문자열 Key로,
     * 각 과제에 대한 진행상황이 학생별로 정리된 데이터를 progresses 문자열 Key로 갖는 Map을 생성합니다.
     *
     * @param course             과목 엔티티
     * @param studentsOfThisCourse 해당 과목의 학생 엔티티 목록
     * @param startAt            시작 날짜
     * @return 특정 날짜에 해당하는 과제 목록과 학생들의 진행 상황
     */
    public Map<String, Object> getTasksAndProgressesByDate(CourseEntity course, List<MemberEntity> studentsOfThisCourse, LocalDate startAt) {
        Map<String, Object> result = new HashMap<>();
        result.put("taskList", getTasksByDate(course, startAt));
        result.put("progresses", getProgressesByDate(studentsOfThisCourse, startAt));
        return result;
    }

    /**
     * 학생이 강의실을 탈퇴할 때, 해당 학생에게 부여된 모든 과제들을 삭제하는 메서드입니다.
     * 탈퇴 시점을 과제의 DeletedAt 필드에 할당하여 저장합니다.
     * Soft deletion
     *
     * @param member 탈퇴하는 학생 엔티티
     */
    public void deleteAssignedTasksOfLeavingStudent(MemberEntity member) {
        LocalDateTime now = LocalDateTime.now();
        List<AssignedTaskEntity> assignedTaskEntityListToBeDeleted = assignedTaskRepository.findAllByMemberAndDeletedAtIsNull(member);
        for (AssignedTaskEntity assignedTaskEntity : assignedTaskEntityListToBeDeleted) {
            assignedTaskEntity.setDeletedAt(now);
            assignedTaskRepository.save(assignedTaskEntity);
        }
    }

    /**
     * 특정 과제의 진행상황을 세부적으로 조회하는 메서드입니다.
     * [완료 학생 수, 진행중인 학생 수, 문제가 발생한 학생 수, 시작전의 학생 수]
     * 특정 과제에 대한 학생들의 진행상황을 조회하여 위의 배열을 생성합니다.
     * swith문을 활용
     *
     * @param task 과제 엔티티
     * @return 진행상황을 담은 정수 배열 반환
     */
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

    /**
     * 학생에게 부여된 과제의 존재를 파악하는 메서드입니다.
     *
     * @param assignedTaskId 부여된 과제의 ID
     * @return 해당 과제 엔티티 객체를 반환
     */
    @Override
    public AssignedTaskEntity checkAssignedTask(Long assignedTaskId) {
        return assignedTaskRepository.findByIdAndDeletedAtIsNull(assignedTaskId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
    }

    /**
     * 학생에게 부여된 과제를 조회하는 메서드입니다.
     *
     * @param assignedTask 부여된 과제 엔티티
     * @return 사용자에게 공개 가능한 정보를 담은 Dto 반환
     */
    @Override
    public TaskOverviewDto getTaskDetails(AssignedTaskEntity assignedTask) {
        return TaskOverviewDto.fromEntity(assignedTask.getTask());
    }

    /**
     * Id로 과제의 존재를 파악하는 메서드입니다.
     *
     * @param taskId 과제의 ID
     * @return 해당 과제 엔티티 객체
     */
    @Override
    public TaskEntity checkTask(Long taskId) {
        return taskRepository.findByIdAndDeletedAtIsNull(taskId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_TASK));
    }

    /**
     * 미완료 과제를 조회하는 메서드입니다.
     * 과제의 진행상황이 "완료"가 아닌 객체들을 조회 후,
     * 사용자에게 공개 가능한 정보만을 담아 dto목록을 생성합니다.
     *
     * @param member 이 강의실에 속한 학생 엔티티
     * @return dto 리스트
     */
    @Override
    public List<AssignedTaskOverviewDto> getUncompletedAssignedTasksListOfThisCourse(MemberEntity member) {
        List<AssignedTaskOverviewDto> result = new ArrayList<>();
        List<AssignedTaskEntity> theUncompleted = assignedTaskRepository.findAllByMemberAndStatusIsNotAndDeletedAtIsNull(member, "완료");
        for (AssignedTaskEntity uncompleted : theUncompleted) result.add(AssignedTaskOverviewDto.fromEntity(uncompleted));
        return result;
    }
}
