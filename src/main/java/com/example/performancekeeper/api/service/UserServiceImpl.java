package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.user.UserCreateDto;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 인터페이스 UserService의 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * UserId로 유저의 존재를 확인하는 메서드입니다.
     * 회원 테이블에서 userId와 일치하는 객체를 찾습니다.
     *
     * @param userId
     * @return 정상 수행 시 해당 UserEntity 객체를 반환, 조회 실패시 에러를 발생시킵니다.
     */
    public UserEntity checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));
    }

    /**
     * 회원 정보를 등록하는 메서드입니다.
     * 파라미터인 UserCreateDto에 담긴 아이디를 가지고 중복 여부를 파악합니다.
     * 파라미터인 UserCreateDto에 담긴 비밀번호, 비밀번호 일치 여부를 확인합니다.
     * 검증이 완료된 아이디와 비밀번호를 바탕으로 새 UserEntity를 생성하여 회원 테이블에 등록합니다.
     *
     * @param userCreateDto
     */
    public void createUser(UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        checkUsername(username); // 아이디 중복 파악
        checkPassword(userCreateDto); // 비밀번호 일치 확인
        UserEntity user = new UserEntity(username, passwordEncoder.encode(userCreateDto.getPassword()), "USER");
        userRepository.save(user); // 데이터 저장
    }

    /**
     * 아이디 중복 검사를 위한 메서드입니다.
     * 회원 테이블에서 입력된 username으로 조회를 요청해 일치하는 값이 있을 경우 에러를 발생합니다.
     *
     * @param username
     */
    private void checkUsername(String username) {
        if (userRepository.findByUsernameAndDeletedAtIsNull(username).isPresent()) throw new CustomException(CustomErrorCode.USERNAME_DUPLICATED);
    }

    /**
     * 비밀번호 일치 여부 검사를 위한 메서드입니다.
     * UserCreateDto에 담긴 password, passwordCheck의 값이 동일하지 않은 경우 에러를 발생합니다.
     *
     * @param userCreateDto 입력된 비밀번호, 비밀번호 확인 값을 담은 DTO 객체
     */
    private void checkPassword(UserCreateDto userCreateDto) {
        String password1 = userCreateDto.getPassword();
        String password2 = userCreateDto.getPasswordCheck();
        if (!password1.equals(password2)) throw new CustomException(CustomErrorCode.PASSWORD_CHECK_MISMATCH);
    }
}
