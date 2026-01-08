package kr.kiomn2.bigtraffic.interfaces.auth.api;

import kr.kiomn2.bigtraffic.application.auth.command.WithdrawalCommand;
import kr.kiomn2.bigtraffic.application.auth.query.GetCurrentUserQuery;
import kr.kiomn2.bigtraffic.application.auth.service.AuthService;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.AuthResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.UserInfoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<AuthResponse> withdrawal(@AuthenticationPrincipal User user, HttpSession session) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "로그인이 필요합니다."));
        }

        WithdrawalCommand command = new WithdrawalCommand(user.getEmail());
        authService.withdrawal(command);

        // 세션 무효화
        session.invalidate();

        return ResponseEntity.ok(new AuthResponse(null, "회원탈퇴가 완료되었습니다."));
    }

    /**
     * 로그아웃 (세션 무효화)
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        // 세션 무효화
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new AuthResponse(null, "로그아웃이 완료되었습니다."));
    }

    /**
     * 세션 유효성 검증
     */
    @SecurityRequirements
    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateSession(@AuthenticationPrincipal User user) {
        if (user != null) {
            return ResponseEntity.ok(new AuthResponse(user.getEmail(), "세션이 유효합니다."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "세션이 유효하지 않습니다."));
        }
    }

    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        GetCurrentUserQuery query = new GetCurrentUserQuery(user.getEmail());
        UserInfoResponse response = authService.getCurrentUser(query);

        return ResponseEntity.ok(response);
    }

    // 카카오 로그인은 Spring Security OAuth2가 자동으로 처리합니다.
    // /oauth2/authorization/kakao 로 리다이렉트하면 자동 로그인 처리됩니다.
}