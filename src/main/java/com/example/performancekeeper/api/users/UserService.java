package com.example.performancekeeper.api.users;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(UserCreateDto userCreateDto) {
        String username = userCreateDto.getUsername();
        checkUsername(username); // 아이디 중복 파악
        checkPassword(userCreateDto); // 비밀번호 일치 확인
        UserEntity user = new UserEntity(username, passwordEncoder.encode(userCreateDto.getPassword()));
        userRepository.save(user); //데이터 저장
    }

    private void checkUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) throw new CustomException(CustomErrorCode.USERNAME_DUPLICATED);
    }

    private void checkPassword(UserCreateDto userCreateDto) {
        String password1 = userCreateDto.getPassword();
        String password2 = userCreateDto.getPasswordCheck();
        if (!password1.equals(password2)) throw new CustomException(CustomErrorCode.PASSWORD_CHECK_MISMATCH);
    }
}
