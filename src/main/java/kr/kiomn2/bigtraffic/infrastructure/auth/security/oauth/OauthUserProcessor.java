package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OauthUserProcessor {
    boolean isProvider(String provider);
    User process(String provider, OAuth2User oAuth2User);
}
