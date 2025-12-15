package kr.kiomn2.bigtraffic.interfaces.auth.api;

import kr.kiomn2.bigtraffic.application.auth.service.AuthService;
import kr.kiomn2.bigtraffic.application.auth.service.JwtBlacklistService;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.JwtTokenProvider;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.AuthResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.TokenValidationResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;


    /**
     * 회원탈퇴
     */
    @DeleteMapping("/withdrawal")
    public ResponseEntity<AuthResponse> withdrawal(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        String email = jwtTokenProvider.getEmail(token);

        authService.withdrawal(email);
        return ResponseEntity.ok(new AuthResponse(null, "회원탈퇴가 완료되었습니다."));
    }

    /**
     * 로그아웃 (JWT 블랙리스트에 추가)
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = authorizationHeader.substring(7); // "Bearer " 제거

        // 토큰을 블랙리스트에 추가
        jwtBlacklistService.addToBlacklist(token, "LOGOUT");

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
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return ResponseEntity.ok(new TokenValidationResponse(false, "Authorization 헤더가 없거나 형식이 올바르지 않습니다."));
            }

            String token = authorization.substring(7); // "Bearer " 제거
            boolean isValid = jwtTokenProvider.validateToken(token);

            if (isValid) {
                // 블랙리스트 체크
                if (jwtBlacklistService.isBlacklisted(token)) {
                    return ResponseEntity.ok(new TokenValidationResponse(false, "블랙리스트에 등록된 토큰입니다."));
                }

                String email = jwtTokenProvider.getEmail(token);
                return ResponseEntity.ok(new TokenValidationResponse(true, email, "유효한 토큰입니다."));
            } else {
                return ResponseEntity.ok(new TokenValidationResponse(false, "유효하지 않은 토큰입니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new TokenValidationResponse(false, "토큰 검증 중 오류가 발생했습니다."));
        }
    }
}