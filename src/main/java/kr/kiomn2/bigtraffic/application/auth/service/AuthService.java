package kr.kiomn2.bigtraffic.application.auth.service;

import kr.kiomn2.bigtraffic.application.auth.command.LogoutCommand;
import kr.kiomn2.bigtraffic.application.auth.command.ProcessKakaoLoginCommand;
import kr.kiomn2.bigtraffic.application.auth.command.WithdrawalCommand;
import kr.kiomn2.bigtraffic.application.auth.query.GetCurrentUserQuery;
import kr.kiomn2.bigtraffic.application.auth.query.ValidateTokenQuery;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.exception.UserNotFoundException;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.JwtTokenProvider;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.UserInfoResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;
    private final KakaoAuthService kakaoAuthService;

    /**
     * 회원탈퇴
     */
    @Transactional
    public void withdrawal(WithdrawalCommand command) {
        log.info("회원탈퇴 처리 시작 - email: {}", command.getEmail());

        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> {
                    log.error("회원탈퇴 실패 - 사용자를 찾을 수 없음, email: {}", command.getEmail());
                    return new UserNotFoundException();
                });

        log.info("사용자 삭제 중 - userId: {}, email: {}", user.getId(), command.getEmail());
        userRepository.delete(user);
        log.info("회원탈퇴 처리 완료 - email: {}", command.getEmail());
    }

    /**
     * 로그아웃
     */
    public void logout(LogoutCommand command) {
        log.info("로그아웃 요청");
        jwtBlacklistService.addToBlacklist(command.getToken(), "LOGOUT");
        log.info("로그아웃 완료 - 토큰이 블랙리스트에 추가됨");
    }

    /**
     * 토큰 유효성 검증
     */
    public TokenValidationResult validateToken(ValidateTokenQuery query) {
        log.debug("토큰 유효성 검증 요청");

        boolean isValid = jwtTokenProvider.validateToken(query.getToken());

        if (!isValid) {
            log.warn("토큰 검증 실패 - 유효하지 않은 토큰");
            return new TokenValidationResult(false, null, "유효하지 않은 토큰입니다.");
        }

        if (jwtBlacklistService.isBlacklisted(query.getToken())) {
            log.warn("토큰 검증 실패 - 블랙리스트에 등록된 토큰");
            return new TokenValidationResult(false, null, "블랙리스트에 등록된 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmail(query.getToken());
        log.info("토큰 검증 성공 - email: {}", email);
        return new TokenValidationResult(true, email, "유효한 토큰입니다.");
    }

    /**
     * 현재 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUser(GetCurrentUserQuery query) {
        log.info("사용자 정보 조회 요청 - email: {}", query.getEmail());

        User user = userRepository.findByEmail(query.getEmail())
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없음 - email: {}", query.getEmail());
                    return new RuntimeException("사용자를 찾을 수 없습니다.");
                });

        log.info("사용자 정보 조회 성공 - userId: {}, email: {}", user.getId(), query.getEmail());
        return UserInfoResponse.from(user);
    }

    /**
     * 카카오 로그인 처리
     */
    public String processKakaoLogin(ProcessKakaoLoginCommand command) {
        log.info("카카오 로그인 콜백 요청 - code: {}", command.getCode());
        String jwtToken = kakaoAuthService.processKakaoLogin(command.getCode());
        log.info("카카오 로그인 성공 - JWT 토큰 발급 완료");
        return jwtToken;
    }

    /**
     * Authorization 헤더에서 토큰 추출
     */
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        return authorizationHeader.substring(7);
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String extractEmailFromToken(String token) {
        return jwtTokenProvider.getEmail(token);
    }

    @Getter
    @RequiredArgsConstructor
    public static class TokenValidationResult {
        private final boolean valid;
        private final String email;
        private final String message;
    }
}