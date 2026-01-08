package kr.kiomn2.bigtraffic.interfaces.auth.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        // 인증된 사용자는 user-info로, 아니면 로그인 페이지로
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/finance/user-info";
        }
        return "redirect:/login";
    }

    // OAuth2 로그인은 Spring Security가 자동으로 처리:
    // - /oauth2/authorization/kakao 로 리다이렉트하면 카카오 로그인 시작
    // - 로그인 성공 시 OAuth2AuthenticationSuccessHandler가 /finance/user-info로 리다이렉트

    // 로그아웃은 SecurityConfig의 .logout() 설정에서 자동 처리:
    // - POST /api/auth/logout 호출 시 세션 무효화 및 /login으로 리다이렉트
}
