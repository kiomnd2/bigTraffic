package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        // JWT AccessToken 생성
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        long expiresIn = jwtTokenProvider.getExpirationTime() / 1000; // 초 단위로 변환

        log.info("OAuth2 로그인 성공 - AccessToken 발급");
        log.info("  - Email: {}", user.getEmail());
        log.info("  - Username: {}", user.getName());
        log.info("  - Provider: {}", user.getProvider());
        log.info("  - TokenType: Bearer");
        log.info("  - ExpiresIn: {} seconds", expiresIn);
        log.debug("  - AccessToken: {}...", accessToken.substring(0, Math.min(20, accessToken.length())));

        // 로컬 사용자 정보 페이지로 리다이렉트 (accessToken을 query parameter로 전달)
        String targetUrl = UriComponentsBuilder.fromUriString("/user-info")
                .queryParam("token", accessToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
