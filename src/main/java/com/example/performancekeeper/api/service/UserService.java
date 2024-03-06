package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.user.UserCreateDto;
import com.example.performancekeeper.api.entity.UserEntity;

/**
 * 유저 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface UserService {
    /**
     * UserId로 유저의 존재를 확인하는 메서드입니다.
     *
     * @param userId
     * @return 정상 수행 시 UserEntity 객체를 반환합니다.
     */
    UserEntity checkUser(Long userId);

    /**
     * 회원을 등록하는 메서드입니다.
     *
     * @param userCreateDto
     */
    void createUser(UserCreateDto userCreateDto);
}
