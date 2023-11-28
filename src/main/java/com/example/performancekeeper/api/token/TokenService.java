package com.example.performancekeeper.api.token;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public String issueAccessToken(LoginRequestDto loginRequestDto) {
        UserEntity user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new CustomException(CustomErrorCode.LOGIN_FAILED_NOT_FOUND_USER));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getEncodedPW()))
            throw new CustomException(CustomErrorCode.LOGIN_FAILED_PASSWORD_MISMATCH);

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken();
        refreshTokenService.saveNewRefreshToken(accessToken, refreshToken, user.getId());
        return accessToken;
    }

    public void deleteToken(String accessToken) {
        refreshTokenService.deleteRefreshToken(accessToken);
    }
}
