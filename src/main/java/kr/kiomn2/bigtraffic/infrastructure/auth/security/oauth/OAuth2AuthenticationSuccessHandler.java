package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("=== OAuth2 로그인 성공 핸들러 시작 ===");

        try {
            // Principal이 User 타입인지 확인
            if (!(authentication.getPrincipal() instanceof User)) {
                log.error("Principal이 User 타입이 아닙니다. Type: {}", authentication.getPrincipal().getClass().getName());
                response.sendRedirect("/login?error=invalid_user_type");
                return;
            }

            User user = (User) authentication.getPrincipal();

            if (user.getId() == null) {
                log.error("사용자 ID가 null입니다. Email: {}", user.getEmail());
                response.sendRedirect("/login?error=invalid_user_data");
                return;
            }

            // 세션에 사용자 정보 저장
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(3600); // 세션 타임아웃: 1시간

            log.info("OAuth2 로그인 성공 - 세션 생성");
            log.info("  - UserId: {}", user.getId());
            log.info("  - Email: {}", user.getEmail());
            log.info("  - Username: {}", user.getName());
            log.info("  - Provider: {}", user.getProvider());
            log.info("  - SessionId: {}", session.getId());

            // 로컬 사용자 정보 페이지로 리다이렉트
            String targetUrl = "/dashboard";
            log.info("리다이렉트 URL: {}", targetUrl);

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            log.info("=== OAuth2 로그인 성공 핸들러 완료 ===");

        } catch (ClassCastException e) {
            log.error("Principal을 User로 캐스팅하는데 실패했습니다.", e);
            response.sendRedirect("/login?error=cast_error");
        } catch (Exception e) {
            log.error("OAuth2 로그인 성공 처리 중 오류 발생", e);
            response.sendRedirect("/login?error=handler_error");
        }
    }
}
