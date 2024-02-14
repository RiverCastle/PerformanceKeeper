package com.example.performancekeeper.api.token;

import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.users.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @InjectMocks
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Test
    @DisplayName("로그인 실패 에러처리 신버전")
    void invalidLoginReqV1() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("test", "test");
        Long now = System.currentTimeMillis();
        try {
            tokenService.issueAccessToken(loginRequestDto);
        } catch (CustomException e) {
            Long end = System.currentTimeMillis();
            System.out.println("로그인 실패 에러 처리 신버전 시간 결과: " + (end - now));
        }
    }

    @Test
    @DisplayName("로그인 실패 에러처리 구버전")
    void invalidLoginReqV0() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("test", "test");
        Long now = System.currentTimeMillis();
        try {
            tokenService.issueAccessTokenOldVersion(loginRequestDto);
        } catch (CustomException e) {
            Long end = System.currentTimeMillis();
            System.out.println("로그인 실패 에러 처리 구버전 시간 결과: " + (end - now));
        }
    }
}