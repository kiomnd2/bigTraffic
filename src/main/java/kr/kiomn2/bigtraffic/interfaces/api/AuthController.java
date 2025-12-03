package kr.kiomn2.bigtraffic.interfaces.api;

import kr.kiomn2.bigtraffic.application.service.AuthService;
import kr.kiomn2.bigtraffic.infrastructure.security.JwtTokenProvider;
import kr.kiomn2.bigtraffic.interfaces.dto.AuthResponse;
import kr.kiomn2.bigtraffic.interfaces.dto.LoginRequest;
import kr.kiomn2.bigtraffic.interfaces.dto.RegisterRequest;
import kr.kiomn2.bigtraffic.interfaces.dto.TokenValidationResponse;
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

    /**
     * 회원가입
     */
    @SecurityRequirements
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request.getEmail(), request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, "회원가입이 완료되었습니다."));
    }

    /**
     * 로그인
     */
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, "로그인이 완료되었습니다."));
    }

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