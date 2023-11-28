package com.example.performancekeeper.api.token;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveNewRefreshToken(String accessToken, String refreshToken, Long userId) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(accessToken, refreshToken, userId);
        refreshTokenRepository.save(refreshTokenEntity);
    }
        public void saveNewAccessTokenInRefreshToken(String reAccessToken, RefreshTokenEntity refreshToken) {
        refreshToken.setAccessToken(reAccessToken);
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String accessToken) {
        refreshTokenRepository.deleteById(accessToken);
    }

    public Optional<RefreshTokenEntity> getRefreshTokenByAccessToken(String accessToken) {
        return refreshTokenRepository.findById(accessToken);
    }
}
