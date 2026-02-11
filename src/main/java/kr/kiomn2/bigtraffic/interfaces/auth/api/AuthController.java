package kr.kiomn2.bigtraffic.interfaces.auth.api;

import kr.kiomn2.bigtraffic.domain.auth.command.WithdrawalCommand;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.query.GetCurrentUserQuery;
import kr.kiomn2.bigtraffic.domain.auth.service.AuthService;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.AuthResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.dto.UserInfoResponse;
import kr.kiomn2.bigtraffic.interfaces.auth.mapper.AuthMapper;
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
    private final AuthMapper authMapper;

    @DeleteMapping("/withdrawal")
    public ResponseEntity<AuthResponse> withdrawal(@AuthenticationPrincipal User user, HttpSession session) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "로그인이 필요합니다."));
        }

        WithdrawalCommand command = new WithdrawalCommand(user.getEmail());
        authService.withdrawal(command);

        session.invalidate();

        return ResponseEntity.ok(new AuthResponse(null, "회원탈퇴가 완료되었습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new AuthResponse(null, "로그아웃이 완료되었습니다."));
    }

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

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        GetCurrentUserQuery query = new GetCurrentUserQuery(user.getEmail());
        User foundUser = authService.getCurrentUser(query);
        UserInfoResponse response = authMapper.toResponse(foundUser);

        return ResponseEntity.ok(response);
    }
}
