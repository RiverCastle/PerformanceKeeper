package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.entity.RefreshTokenEntity;
import com.example.performancekeeper.api.repository.RefreshTokenRepository;
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
