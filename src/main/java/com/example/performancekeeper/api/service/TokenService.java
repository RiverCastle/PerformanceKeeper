package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.token.LoginRequestDto;
import com.example.performancekeeper.api.common.TokenProvider;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * JWT 관련 비즈니스 로직을 처리하는 Service 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public String issueAccessToken(LoginRequestDto loginRequestDto) {
        checkUsername(loginRequestDto.getUsername());
        checkPassword(loginRequestDto.getPassword());
        UserEntity user = userRepository.findByUsernameAndDeletedAtIsNull(loginRequestDto.getUsername())
                .orElseThrow(() -> new CustomException(CustomErrorCode.LOGIN_FAILED_NOT_FOUND_USER));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getEncodedPW()))
            throw new CustomException(CustomErrorCode.LOGIN_FAILED_PASSWORD_MISMATCH);

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken();
        refreshTokenService.saveNewRefreshToken(accessToken, refreshToken, user.getId());
        return accessToken;
    }

    public String issueAccessTokenOldVersion(LoginRequestDto loginRequestDto) {
        UserEntity user = userRepository.findByUsernameAndDeletedAtIsNull(loginRequestDto.getUsername())
                .orElseThrow(() -> new CustomException(CustomErrorCode.LOGIN_FAILED_NOT_FOUND_USER));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getEncodedPW()))
            throw new CustomException(CustomErrorCode.LOGIN_FAILED_PASSWORD_MISMATCH);

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken();
        refreshTokenService.saveNewRefreshToken(accessToken, refreshToken, user.getId());
        return accessToken;
    }

    private void checkUsername(String username) {
        if (username.length() < 5) throw new CustomException(CustomErrorCode.UNVALID_LOGIN);
    }
    private void checkPassword(String password) {
        if (password.length() < 8) throw new CustomException(CustomErrorCode.UNVALID_LOGIN);
    }

    public void deleteToken(String accessToken) {
        refreshTokenService.deleteRefreshToken(accessToken);
    }
}
