package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth.process;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth.OauthUserProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class OauthUserProcessorImpl implements OauthUserProcessor {

    @Override
    public boolean isProvider(String provider) {
        return "kakao".equals(provider);
    }

    @Override
    public User process(String provider, OAuth2User oAuth2User) {

        Long kakaoId = oAuth2User.getAttribute("id");
        Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        log.info("카카오 로그인 - ID: {}, Email: {}, Nickname: {}", kakaoId, email, nickname);

        // User 엔티티 생성
        return User.builder()
                .email(email)
                .username(nickname)
                .provider(provider)
                .providerId(String.valueOf(kakaoId))
                .build();
    }
}
