package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberServiceImpl;
import com.example.performancekeeper.api.task.AssignedTaskEntity;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserServiceImpl userServiceImpl;
    private final CourseServiceImpl courseServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final TaskService taskService;

    @Override
    public void createComment(Long userId, Long courseId, Long assignedTaskId, CommentCreateDto commentCreateDto) {
        UserEntity user = userServiceImpl.checkUserEntity(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.getAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager")) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        commentRepository.save(new CommentEntity(commentCreateDto.getContent(), assignedTask, member));
    }
}
