package com.example.performancekeeper.api.token;

import com.example.performancekeeper.api.users.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;


@Component
public class TokenProvider {
    private final Key key;
    private final JwtParser jwtParser;
    private Long accessTokenValidTime;
    private Long refreshTokenValidTime;

    public TokenProvider(@Value("${jwt.secret}") String key,
                         @Value("${jwt.token.access-token-valid-time}") Long accessTokenValidTime,
                         @Value("${jwt.token.refresh-token-valid-time}") Long refreshTokenValidTime) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }

    public String createAccessToken(UserEntity user) {
        return Jwts.builder()
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(accessTokenValidTime)))
                .claim("id", user.getId())
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = Jwts
                .claims();

        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .signWith(key)
                .compact();
    }

    public void validToken(String accessToken) {
        jwtParser.parseClaimsJws(accessToken);

    }

    public boolean validateRefreshToken(String refreshToken) {
        jwtParser.parseClaimsJws(refreshToken);
        return true;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaims(accessToken);
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
        return new UsernamePasswordAuthenticationToken(claims.get("id"), null, authorities);
    }

    public Claims getClaims(String token) {
        return jwtParser.parseClaimsJws(token)
                .getBody();
    }
}
