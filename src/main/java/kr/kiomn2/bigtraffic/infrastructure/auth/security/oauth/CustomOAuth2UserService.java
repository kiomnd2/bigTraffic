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
        log.debug("OAuth2 사용자 정보 로드 시작");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("OAuth2 로그인 시도 - provider: {}", registrationId);

        // OAuth2User를 우리의 User 엔티티로 변환
        for (OauthUserProcessor userProcessor : oauthUserProcessors) {
            if (userProcessor.isProvider(registrationId)) {
                log.debug("OAuth 프로세서 발견 - provider: {}", registrationId);
                User user = userProcessor.process(registrationId, oAuth2User);

                // 사용자 저장 또는 업데이트
                String providerId = user.getProviderId();
                log.debug("사용자 정보 처리 완료 - email: {}, providerId: {}", user.getEmail(), providerId);

                Optional<User> existingUser = userRepository.findByProviderAndProviderId(registrationId, providerId);

                if (existingUser.isPresent()) {
                    User loginUser = existingUser.get();
                    loginUser.updateLastLoginDate();
                    User updatedUser = userRepository.save(loginUser);
                    log.info("기존 사용자 로그인 - userId: {}, email: {}, lastLogin: {}", updatedUser.getId(), updatedUser.getEmail(), updatedUser.getLastLoginDate());
                    return updatedUser;
                } else {
                    user.updateLastLoginDate();
                    User savedUser = userRepository.save(user);
                    log.info("신규 사용자 생성 - userId: {}, email: {}, provider: {}", savedUser.getId(), savedUser.getEmail(), registrationId);
                    return savedUser;
                }
            }
        }
        log.error("OAuth 프로세스를 찾을 수 없음 - provider: {}", registrationId);
        throw new IllegalArgumentException("Oauth 프로세스를 찾을 수 없습니다");
    }
}
