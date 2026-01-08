package kr.kiomn2.bigtraffic.interfaces.auth.api;

import kr.kiomn2.bigtraffic.application.auth.command.LogoutCommand;
import kr.kiomn2.bigtraffic.application.auth.command.ProcessKakaoLoginCommand;
import kr.kiomn2.bigtraffic.application.auth.command.WithdrawalCommand;
import kr.kiomn2.bigtraffic.application.auth.query.GetCurrentUserQuery;
import kr.kiomn2.bigtraffic.application.auth.query.ValidateTokenQuery;
import kr.kiomn2.bigtraffic.application.auth.service.AuthService;
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


    /**
     * 회원탈퇴
     */
    @DeleteMapping("/withdrawal")
    public ResponseEntity<AuthResponse> withdrawal(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authService.extractToken(authorizationHeader);
        String email = authService.extractEmailFromToken(token);

        WithdrawalCommand command = new WithdrawalCommand(email);
        authService.withdrawal(command);

        return ResponseEntity.ok(new AuthResponse(null, "회원탈퇴가 완료되었습니다."));
    }

    /**
     * 로그아웃 (JWT 블랙리스트에 추가)
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = authService.extractToken(authorizationHeader);

        LogoutCommand command = new LogoutCommand(token);
        authService.logout(command);

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

            String token = authService.extractToken(authorization);
            ValidateTokenQuery query = new ValidateTokenQuery(token);
            AuthService.TokenValidationResult result = authService.validateToken(query);

            if (result.isValid()) {
                return ResponseEntity.ok(new TokenValidationResponse(true, result.getEmail(), result.getMessage()));
            } else {
                return ResponseEntity.ok(new TokenValidationResponse(false, result.getMessage()));
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
        String token = authService.extractToken(authorizationHeader);
        String email = authService.extractEmailFromToken(token);

        GetCurrentUserQuery query = new GetCurrentUserQuery(email);
        UserInfoResponse response = authService.getCurrentUser(query);

        return ResponseEntity.ok(response);
    }

    /**
     * 카카오 로그인 콜백 처리
     */
    @SecurityRequirements
    @PostMapping("/kakao/callback")
    public ResponseEntity<KakaoLoginResponse> kakaoCallback(@RequestBody KakaoCallbackRequest request) {
        try {
            ProcessKakaoLoginCommand command = new ProcessKakaoLoginCommand(request.getCode());
            String jwtToken = authService.processKakaoLogin(command);
            return ResponseEntity.ok(new KakaoLoginResponse(jwtToken, "로그인 성공"));
        } catch (Exception e) {
            log.error("카카오 로그인 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new KakaoLoginResponse(null, "로그인 실패: " + e.getMessage()));
        }
    }
}