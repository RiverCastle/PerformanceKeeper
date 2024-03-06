package com.example.performancekeeper.api.dto.course;

import lombok.Data;

/**
 * 자신이 속한 강의실에 대한 기본 정보가 담기는 Dto 클래스입니다.
 */
@Data
public class MyCourseOverviewDto {
    private Long id;
    private String name;
    private String role;
    private int progress;

    /**
     * MyCourseOverviewDto 생성자입니다.
     * 진척도는 생성자에서 초기화되지 않습니다.
     *
     * @param id 강의실 id
     * @param name 강의실 이름
     * @param role 강의실에서의 역할
     */
    public MyCourseOverviewDto(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
