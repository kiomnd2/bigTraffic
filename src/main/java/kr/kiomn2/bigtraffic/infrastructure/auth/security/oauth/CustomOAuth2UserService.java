package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final List<OauthUserProcessor> oauthUserProcessors;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("OAuth2 로그인 시도: provider={}", registrationId);

        // OAuth2User를 우리의 User 엔티티로 변환
        for (OauthUserProcessor userProcessor : oauthUserProcessors) {
            if (userProcessor.isProvider(registrationId)) {
                User user = userProcessor.process(registrationId, oAuth2User);

                // 사용자 저장 또는 업데이트
                String providerId = user.getProviderId();
                Optional<User> existingUser = userRepository.findByProviderAndProviderId(registrationId, providerId);

                // 생성
                return existingUser.orElseGet(() -> userRepository.save(user));
            }
        }
        throw new IllegalArgumentException("Oauth 프로세스를 찾을 수 없습니다");
    }
}
