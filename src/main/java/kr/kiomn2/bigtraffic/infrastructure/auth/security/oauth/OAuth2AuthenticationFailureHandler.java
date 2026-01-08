package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // 에러 로깅
        log.error("=== OAuth2 로그인 실패 ===");
        log.error("Error Type: {}", exception.getClass().getSimpleName());
        log.error("Error Message: {}", exception.getMessage());
        log.error("Request URI: {}", request.getRequestURI());
        log.error("Request Method: {}", request.getMethod());
        log.error("Remote Address: {}", request.getRemoteAddr());

        // 스택 트레이스 출력
        log.error("StackTrace: ", exception);

        // Cause 체인 로깅
        Throwable cause = exception.getCause();
        int depth = 0;
        while (cause != null && depth < 3) {
            log.error("Caused by [{}]: {} - {}", depth, cause.getClass().getName(), cause.getMessage());
            cause = cause.getCause();
            depth++;
        }

        log.error("============================");

        // 에러 메시지를 포함한 리다이렉트 URL 생성
        String targetUrl = UriComponentsBuilder.fromUriString("/login")
                .queryParam("error", "true")
                .queryParam("message", exception.getMessage())
                .build().toUriString();

        log.info("리다이렉트 URL: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
