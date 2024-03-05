package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.repository.CourseRepository;
import com.example.performancekeeper.api.dto.course.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 인터페이스 CourseService의 구현체 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    /**
     * 새로운 강의실을 생성하는 메서드입니다.
     * 암호화되지 않은 참여코드를 암호화하여 JoinCdoe에 재할당한 후,
     * 새로운 CourseEntity를 생성하여 강의실 테이블에 저장합니다.
     *
     * @param courseCreateDto
     * @return 새로 생성된 CourseEntity 반환
     */
    public CourseEntity createCourse(CourseCreateDto courseCreateDto) {
        courseCreateDto.setJoinCode(passwordEncoder.encode(courseCreateDto.getJoinCode()));
        return courseRepository.save(CourseCreateDto.toEntity(courseCreateDto));
    }

    /**
     * 입력된 키워드를 포함하는 이름을 가진 강의실을 조회하는 메서드입니다.
     * 강의실 테이블의 name 컬럼에서 키워드와 일치하는 객체들을 조회한 후,
     * 일반 사용자에게 공개가능한 정보만을 가지고 dto 객체를 생성하여 리스트에 추가합니다.
     *
     * @param keyword 검색 키워드
     * @return 검색 키워드를 포함하는 이름을 가진 CourseEntity의 대략적인 정보를 가진 dto 리스트 반환
     */
    public List<CourseOverviewDto> searchCourse(String keyword) {
        List<CourseEntity> courseEntities = courseRepository.findAllByNameContainingAndDeletedAtIsNull(keyword);
        List<CourseOverviewDto> result = new ArrayList<>();
        for (CourseEntity entity : courseEntities) result.add(CourseOverviewDto.fromEntity(entity));
        return result;
    }

    /**
     * 특정 강의실의 세부정보를 조회하는 메서드입니다.
     *
     * @param course
     * @return 사용자에게 공개 가능한 강의실 세부정보를 담은 dto 객체 반환
     */
    public CourseDetailsDto getCourseDetails(CourseEntity course) {
        return CourseDetailsDto.fromEntity(course);
    }

    /**
     * 강의실의 이름을 변경하는 메서드입니다.
     * 새 이름을 기존의 CourseEntity의 name 필드에 할당한 후 저장
     *
     * @param course
     * @param courseNameUpdateDto 새로운 이름이 담긴 dto 객체
     */
    public void updateCourseName(CourseEntity course, CourseNameUpdateDto courseNameUpdateDto) {
        course.setName(courseNameUpdateDto.getNewName());
        courseRepository.save(course);
    }

    /**
     * 강의실의 소개 내용을 변경하는 메서드입니다.
     * 새 소개 내용을 기존의 CourseEntity의 description 필드에 할당한 후 저장
     *
     * @param course
     * @param courseDescriptionUpdateDto 새로운 소개 내용이 담긴 dto 객체
     */
    public void updateCourseDescription(CourseEntity course, CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        course.setDescription(courseDescriptionUpdateDto.getNewDescription());
        courseRepository.save(course);
    }

    /**
     * 강의실 존재 여부를 파악하는 메서드입니다.
     * CourseEntity 객체의 DeletedAt 필드가 Null인 경우, 현재 존재한다고 파악합니다.
     *
     * @param courseId
     * @return 정상 수행 시, 해당 CourseEntity 반환, 조회 실패시 에러 발생
     * @deprecated
     */
    public CourseEntity checkCourseEntity(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }
    /**
     * 강의실 존재 여부를 파악하는 메서드입니다.
     * CourseEntity 객체의 DeletedAt 필드가 Null인 경우, 현재 존재한다고 파악합니다.
     *
     * @param courseId
     * @return 정상 수행 시, 해당 CourseEntity 반환, 조회 실패시 에러 발생
     */
    @Override
    public CourseEntity checkCourse(Long courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COURSE));
    }
}
