package com.example.performancekeeper.api.users;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;

    public UserEntity checkUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_USER));
    }
}
