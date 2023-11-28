package com.example.performancekeeper.api.token;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = authHeader.split(" ")[1];
        Optional<RefreshTokenEntity> optionalRefreshTokenEntity = refreshTokenService.getRefreshTokenByAccessToken(accessToken);
        if (optionalRefreshTokenEntity.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            tokenProvider.validToken(accessToken);
        } catch (ExpiredJwtException e) { // 엑세스 토큰이 만료된 경우 => Refresh 토큰 조회
            RefreshTokenEntity refreshToken = optionalRefreshTokenEntity.get();
            if (tokenProvider.validateRefreshToken(refreshToken.getRefreshToken())) { // Refresh 토큰이 유효한 경우
                UserEntity user = userRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new CustomException(CustomErrorCode.LOGIN_FAILED_NOT_FOUND_USER));
                accessToken = tokenProvider.createAccessToken(user); // 새 엑세스 토큰 생성
                refreshTokenService.saveNewAccessTokenInRefreshToken(accessToken, refreshToken);
            } else { // Refresh 토큰이 무효한 경우
                filterChain.doFilter(request, response);
                return;
            }
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(tokenProvider.getAuthentication(accessToken));
        SecurityContextHolder.setContext(context);
        response.setHeader("Authorization", "Bearer " + accessToken);
        filterChain.doFilter(request, response);
    }
}
