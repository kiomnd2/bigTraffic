package kr.kiomn2.bigtraffic.interfaces.auth.api;

import kr.kiomn2.bigtraffic.application.auth.service.AuthService;
import kr.kiomn2.bigtraffic.application.auth.service.JwtBlacklistService;
import kr.kiomn2.bigtraffic.application.auth.service.KakaoAuthService;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.JwtTokenProvider;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.AuthResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.KakaoCallbackRequest;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.KakaoLoginResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.TokenValidationResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;
    private final UserRepository userRepository;
    private final KakaoAuthService kakaoAuthService;


    /**
     * 회원탈퇴
     */
    @DeleteMapping("/withdrawal")
    public ResponseEntity<AuthResponse> withdrawal(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        String email = jwtTokenProvider.getEmail(token);

        log.info("회원탈퇴 요청 - email: {}", email);
        authService.withdrawal(email);
        log.info("회원탈퇴 완료 - email: {}", email);

        return ResponseEntity.ok(new AuthResponse(null, "회원탈퇴가 완료되었습니다."));
    }

    /**
     * 로그아웃 (JWT 블랙리스트에 추가)
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = authorizationHeader.substring(7); // "Bearer " 제거

        log.info("로그아웃 요청");
        // 토큰을 블랙리스트에 추가
        jwtBlacklistService.addToBlacklist(token, "LOGOUT");
        log.info("로그아웃 완료 - 토큰이 블랙리스트에 추가됨");

        return ResponseEntity.ok(new AuthResponse(null, "로그아웃이 완료되었습니다."));
    }

    /**
     * 토큰 유효성 검증
     */
    @SecurityRequirements
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            log.debug("토큰 유효성 검증 요청");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.warn("토큰 검증 실패 - Authorization 헤더 없음 또는 형식 오류");
                return ResponseEntity.ok(new TokenValidationResponse(false, "Authorization 헤더가 없거나 형식이 올바르지 않습니다."));
            }

            String token = authorization.substring(7); // "Bearer " 제거
            boolean isValid = jwtTokenProvider.validateToken(token);

            if (isValid) {
                // 블랙리스트 체크
                if (jwtBlacklistService.isBlacklisted(token)) {
                    log.warn("토큰 검증 실패 - 블랙리스트에 등록된 토큰");
                    return ResponseEntity.ok(new TokenValidationResponse(false, "블랙리스트에 등록된 토큰입니다."));
                }

                String email = jwtTokenProvider.getEmail(token);
                log.info("토큰 검증 성공 - email: {}", email);
                return ResponseEntity.ok(new TokenValidationResponse(true, email, "유효한 토큰입니다."));
            } else {
                log.warn("토큰 검증 실패 - 유효하지 않은 토큰");
                return ResponseEntity.ok(new TokenValidationResponse(false, "유효하지 않은 토큰입니다."));
            }
        } catch (Exception e) {
            log.error("토큰 검증 중 오류 발생", e);
            return ResponseEntity.ok(new TokenValidationResponse(false, "토큰 검증 중 오류가 발생했습니다."));
        }
    }

    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        String email = jwtTokenProvider.getEmail(token);

        log.info("사용자 정보 조회 요청 - email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없음 - email: {}", email);
                    return new RuntimeException("사용자를 찾을 수 없습니다.");
                });

        log.info("사용자 정보 조회 성공 - userId: {}, email: {}", user.getId(), email);
        return ResponseEntity.ok(UserInfoResponse.from(user));
    }

    /**
     * 카카오 로그인 콜백 처리
     */
    @SecurityRequirements
    @PostMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestBody KakaoCallbackRequest request) {
        try {
            log.info("카카오 로그인 콜백 요청 - code: {}", request.getCode());
            String jwtToken = kakaoAuthService.processKakaoLogin(request.getCode());
            log.info("카카오 로그인 성공 - JWT 토큰 발급 완료");
            return ResponseEntity.ok(new KakaoLoginResponse(jwtToken, "로그인 성공"));
        } catch (Exception e) {
            log.error("카카오 로그인 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new KakaoLoginResponse(null, "로그인 실패: " + e.getMessage()));
        }
    }
}