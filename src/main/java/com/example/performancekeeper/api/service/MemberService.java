package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.member.JoinCourseDto;
import com.example.performancekeeper.api.dto.member.LeaveRequestDto;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.dto.member.NicknameUpdateDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.UserEntity;

import java.util.List;
/**
 * 강의실 멤버 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface MemberService {
    /**
     * 사용자가 강의실의 매니저인지 파악하는 메서드
     */
    void checkManagerMember(UserEntity user, CourseEntity course);

    /**
     * 강의실에 속한 모든 학생 멤버를 가져오는 메서드
     * @param course
     * @return MemberEntity 객체들을 List에 담아 반환
     */
    List<MemberEntity> getAllStudentsOfThisCourse(CourseEntity course);

    /**
     * 사용자가 강의실의 학생인지 파악하는 메서드
     *
     * @param user
     * @param course
     * @return 정상 수행 시, 해당 학생 MemberEntity를 반환
     */
    MemberEntity checkStudentMember(UserEntity user, CourseEntity course);

    /**
     * 사용자가 강의실에 입실한 멤버인지(학생|매니저 구분X) 파악하는 메서드
     *
     * @param user
     * @param course
     * @return 정상 수행 시, 해당 MemberEntity를 반환
     */
    MemberEntity checkMember(UserEntity user, CourseEntity course);

    /**
     * 사용자를 강의실의 매니저 Member로 등록하는 메서드
     *
     * @param user
     * @param course
     */
    void createManagerMember(UserEntity user, CourseEntity course);

    /**
     * 사용자를 강의실의 학생 Member로 등록하는 메서드
     * @param user
     * @param course
     * @param joinCourseDto 강의실 입실에 필요한 참여코드를 담은 Dto 객체
     * @return 새로 생성된 학생 MemberEntity 객체 반환
     */
    MemberEntity createStudentMember(UserEntity user, CourseEntity course, JoinCourseDto joinCourseDto);

    /**
     * 사용자와 관련된 모든 Member 정보를 조회하는 메서드
     * @param user
     * @return MemberEntity 객체  List 반환
     */
    List<MemberEntity> getMyMember(UserEntity user);

    /**
     * 사용자가 강의실을 나갈 때, 해당 학생 MemberEntity 객체를 삭제하는 메서드
     * @param member
     */
    void deleteStudentMember(MemberEntity member);

    /**
     * MemberEntity 객체의 Nickname 필드를 수정하는 메서드
     * @param member
     * @param nicknameUpdateDto 새 Nickname을 담은 Dto 객체
     */
    void changeNickname(MemberEntity member, NicknameUpdateDto nicknameUpdateDto);

    /**
     * 특정 멤버의 정보를 조회하는 메서드
     * @param member
     * @return 사용자에게 공개 가능한 대략적인 정보를 MemberEntity 객체로부터 dto를 생성해 반환
     */
    MemberOverviewDto getMemberInfo(MemberEntity member);

    /**
     * 학생 MemberEntity 객체를 삭제 전에 확인하는 메서드
     *
     * @param user
     * @param course
     * @param leaveRequestDto
     * @return 학생 MemberEntity 객체를 반환
     */
    MemberEntity checkStudentMemberBeforeDelete(UserEntity user, CourseEntity course, LeaveRequestDto leaveRequestDto);
}
