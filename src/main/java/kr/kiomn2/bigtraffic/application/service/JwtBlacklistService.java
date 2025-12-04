package kr.kiomn2.bigtraffic.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import kr.kiomn2.bigtraffic.domain.entity.BlacklistedToken;
import kr.kiomn2.bigtraffic.infrastructure.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final SecretKey secretKey;

    public JwtBlacklistService(BlacklistedTokenRepository blacklistedTokenRepository,
                               @Value("${jwt.secret}") String secret) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토큰을 블랙리스트에 추가
     */
    @Transactional
    public void addToBlacklist(String token, String reason) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            Date expiration = claims.getExpiration();
            LocalDateTime expirationDate = expiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .token(token)
                    .email(email)
                    .expirationDate(expirationDate)
                    .reason(reason != null ? reason : "LOGOUT")
                    .build();

            blacklistedTokenRepository.save(blacklistedToken);
            log.info("Token blacklisted for user: {}, reason: {}", email, reason);
        } catch (Exception e) {
            log.error("Failed to add token to blacklist", e);
            throw new RuntimeException("Failed to blacklist token", e);
        }
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인 (만료일자도 체크)
     */
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        // 토큰이 블랙리스트에 있고, 아직 만료되지 않은 경우만 true 반환
        return blacklistedTokenRepository.existsByTokenAndExpirationDateAfter(token, LocalDateTime.now());
    }

    /**
     * 만료된 토큰 정리 (매일 자정에 실행)
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired blacklisted tokens");
        blacklistedTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Expired blacklisted tokens cleanup completed");
    }
}
