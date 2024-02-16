package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.user.UserCreateDto;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));
    }

    public void createUser(UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        checkUsername(username); // 아이디 중복 파악
        checkPassword(userCreateDto); // 비밀번호 일치 확인
        UserEntity user = new UserEntity(username, passwordEncoder.encode(userCreateDto.getPassword()), "USER");
        userRepository.save(user); // 데이터 저장
    }

    private void checkUsername(String username) {
        if (userRepository.findByUsernameAndDeletedAtIsNull(username).isPresent()) throw new CustomException(CustomErrorCode.USERNAME_DUPLICATED);
    }

    private void checkPassword(UserCreateDto userCreateDto) {
        String password1 = userCreateDto.getPassword();
        String password2 = userCreateDto.getPasswordCheck();
        if (!password1.equals(password2)) throw new CustomException(CustomErrorCode.PASSWORD_CHECK_MISMATCH);
    }
}
