package kr.kiomn2.bigtraffic.interfaces.api;

import kr.kiomn2.bigtraffic.application.service.AuthService;
import kr.kiomn2.bigtraffic.infrastructure.security.JwtTokenProvider;
import kr.kiomn2.bigtraffic.interfaces.dto.AuthResponse;
import kr.kiomn2.bigtraffic.interfaces.dto.LoginRequest;
import kr.kiomn2.bigtraffic.interfaces.dto.RegisterRequest;
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
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request.getEmail(), request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, "회원가입이 완료되었습니다."));
    }

    /**
     * 로그인
     */
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
}