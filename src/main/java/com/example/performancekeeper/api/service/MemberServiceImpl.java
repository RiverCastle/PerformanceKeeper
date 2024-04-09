package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.member.JoinCourseDto;
import com.example.performancekeeper.api.dto.member.LeaveRequestDto;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.dto.member.NicknameUpdateDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.repository.MemberRepository;
import com.example.performancekeeper.api.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 강의실 멤버 관련 비즈니스 로직을 처리하는 서비스 인터페이스 MemberService의 구현체 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 사용자가 강의실의 매니저인지 파악하는 메서드
     * Members 테이블에서 user와 course 객체에 해당하고, Role 필드의 값이 Manager인 객체를 조회
     * 조회 실패 시, 에러 발생
     *
     * @param user
     * @param course
     */
    public void checkManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Manager")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_MANAGER));
    }

    /**
     * 강의실에 속한 모든 학생 멤버를 가져오는 메서드
     * Members 테이블에서 course 객체에 해당하고 Role 필드의 값이 Student인 객체를 조회
     *
     * @param course
     * @return 조회된 MemberEntity 객체들이 List에 담겨 반환
     */
    public List<MemberEntity> getAllStudentsOfThisCourse(CourseEntity course) {
        return memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(course, "Student");
    }

    /**
     * 사용자가 강의실의 학생인지 파악하는 메서드
     * Members 테이블에서 user, course 객체에 해당하고, Role이 Student인 객체를 조회
     *
     * @param user
     * @param course
     * @return 조회 성공 시, 해당 학생 MemeberEntity 객체를 반환. 실패 시, 에러 발생
     */
    public MemberEntity checkStudentMember(UserEntity user, CourseEntity course) {
        return memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_STUDENT));
    }

    /**
     * 사용자가 강의실에 입실한 멤버인지(학생|매니저 구분X) 파악하는 메서드
     * Members 테이블에서 user, course 객체에 해당하는 MemberEntity 객체 조회
     *
     * @param user
     * @param course
     * @return 조회 성공시, 해당 MemberEntity 객체 반환. 실패 시, 에러 발생
     */
    public MemberEntity checkMember(UserEntity user, CourseEntity course) {
        return memberRepository.findByUserAndCourseAndDeletedAtIsNull(user, course)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_MEMBER));
    }

    /**
     * 사용자가 강의실에 입실한 멤버인지(학생|매니저 구분X) 파악하는 메서드
     * Role 필드가 Manager인 새로운 MemberEntity 객체를 생성 및 Members 테이블에 등록
     *
     * @param user
     * @param course
     */
    public void createManagerMember(UserEntity user, CourseEntity course) {
        memberRepository.save(new MemberEntity(user, course, "Manager"));
    }

    /**
     * 사용자를 강의실의 매니저 Member로 등록하는 메서드
     * 입력된 참여코드와 강의실에 등록된 참여코드의 일치여부 확인 (불일치일 경우 에러발생)
     * 해당 사용자가 해당 강의실에 이미 학생으로 등록되어 있는지 확인 (등록된 경우 에러발생)
     * Role 필드가 Student인 새로운 MemberEntity 객체를 생성 후 Members 테이블에 등록
     *
     * @param user
     * @param course
     * @param joinCourseDto 강의실 입실에 필요한 참여코드를 담은 Dto 객체
     * @return 새로 생성된 학생 MemberEntity 객체 반환
     */
    public MemberEntity createStudentMember(UserEntity user, CourseEntity course, JoinCourseDto joinCourseDto) {
        if (!passwordEncoder.matches(joinCourseDto.getJoinCode(), course.getJoinCode()))
            throw new CustomException(CustomErrorCode.JOINCODE_MISMATCH); // 참여코드 일치 확인
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student");
        if (optionalMemberEntity.isPresent()) throw new CustomException(CustomErrorCode.ALREADY_JOIN);
        return memberRepository.save(new MemberEntity(user, course, "Student"));
    }

    /**
     * 사용자와 관련된 모든 Member 정보를 조회하는 메서드
     * Members 테이블에서 user 객체와 관련된 모든 MemberEntity를 조회
     *
     * @param user
     * @return 조회된 MemberEntity 객체 List 반환
     */
    public List<MemberEntity> getMyMember(UserEntity user) {
        return memberRepository.findAllByUserAndDeletedAtIsNull(user);
    }

    /**
     * 사용자가 강의실을 나갈 때, 해당 학생 MemberEntity 객체를 삭제하는 메서드
     * 현재 시각을 해당 member객체의 DeletedAt 필드에 할당 후 저장
     * Soft delete
     *
     * @param member
     */
    public void deleteStudentMember(MemberEntity member) {
        member.setDeletedAt(LocalDateTime.now());
        memberRepository.save(member); // soft deletion
    }

    /**
     * MemberEntity 객체의 Nickname 필드를 수정하는 메서드
     * member 객체 Nickname 필드의 값을 새로운 값으로 할당 및 저장
     *
     * @param member
     * @param nicknameUpdateDto 새 Nickname을 담은 Dto 객체
     */
    public void changeNickname(MemberEntity member, NicknameUpdateDto nicknameUpdateDto) {
        checkNicknameAvailability(member, nicknameUpdateDto);
        member.setNickname(nicknameUpdateDto.getNickname());
        memberRepository.save(member);
    }

    /**
     * 강의실 내 별명의 중복 여부를 파악하는 메서드
     * Members 테이블에서 같은 course와 연관된 객체들의 Nickname과 일치여부 확인 (일치할 경우 에러 발생)
     *
     * @param member
     * @param nicknameUpdateDto
     */
    private void checkNicknameAvailability(MemberEntity member, NicknameUpdateDto nicknameUpdateDto) {
        List<MemberEntity> memberEntityList = memberRepository.findAllByCourseAndRoleAndDeletedAtIsNull(member.getCourse(), "Student");
        for (MemberEntity memberEntity : memberEntityList)
            if (memberEntity.getNickname().equals(nicknameUpdateDto.getNickname())) throw new CustomException(CustomErrorCode.NICKNAME_DUPLICATED);
    }

    /**
     * 특정 멤버의 정보를 조회하는 메서드
     *
     * @param member
     * @return 사용자에게 공개 가능한 정보를 MemberEntity 객체로부터 Dto를 생성하여 반환
     */
    public MemberOverviewDto getMemberInfo(MemberEntity member) {
        return MemberOverviewDto.fromEntity(member);
    }

    /**
     * 학생 MemberEntity 객체를 삭제 전에 확인하는 메서드
     * Members 테이블에서 user, course 객체와 연관되고 Role 필드의 값이 Student인 객체를 조회
     *
     * @param user
     * @param course
     * @param leaveRequestDto
     * @return 조회 성공 시, 해당 MemberEntity 객체 반환. 실패 시, 에러 발생
     */
    @Override
    public MemberEntity checkStudentMemberBeforeDelete(UserEntity user, CourseEntity course, LeaveRequestDto leaveRequestDto) {
        if (!course.getName().equals(leaveRequestDto.getCourseNameCheck())) throw new CustomException(CustomErrorCode.WRONG_COURSE_NAME);
        return memberRepository.findByUserAndCourseAndRoleAndDeletedAtIsNull(user, course, "Student")
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_STUDENT));
    }
}
