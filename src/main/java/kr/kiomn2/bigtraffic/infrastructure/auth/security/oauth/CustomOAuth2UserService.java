package kr.kiomn2.bigtraffic.infrastructure.auth.security.oauth;

import kr.kiomn2.bigtraffic.domain.accountbook.service.CategoryService;
import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.domain.auth.repository.UserRepository;
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
    private final CategoryService categoryService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("=== OAuth2 사용자 정보 로드 시작 ===");

        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            log.info("OAuth2 로그인 시도");
            log.info("  - Provider: {}", registrationId);
            log.info("  - ClientName: {}", userRequest.getClientRegistration().getClientName());

            // OAuth2User를 우리의 User 엔티티로 변환
            for (OauthUserProcessor userProcessor : oauthUserProcessors) {
                if (userProcessor.isProvider(registrationId)) {
                    log.info("OAuth 프로세서 발견 - provider: {}", registrationId);

                    User user;
                    try {
                        user = userProcessor.process(registrationId, oAuth2User);
                        log.info("사용자 정보 처리 완료 - email: {}, providerId: {}", user.getEmail(), user.getProviderId());
                    } catch (Exception e) {
                        log.error("사용자 정보 처리 실패 - provider: {}", registrationId, e);
                        throw new OAuth2AuthenticationException("사용자 정보 처리 중 오류가 발생했습니다: " + e.getMessage());
                    }

                    // 사용자 저장 또는 업데이트
                    String providerId = user.getProviderId();

                    try {
                        Optional<User> existingUser = userRepository.findByProviderAndProviderId(registrationId, providerId);

                        if (existingUser.isPresent()) {
                            User loginUser = existingUser.get();
                            log.info("기존 사용자 확인 - userId: {}, email: {}", loginUser.getId(), loginUser.getEmail());

                            loginUser.updateLastLoginDate();
                            User updatedUser = userRepository.save(loginUser);

                            log.info("기존 사용자 로그인 성공");
                            log.info("  - UserId: {}", updatedUser.getId());
                            log.info("  - Email: {}", updatedUser.getEmail());
                            log.info("  - Provider: {}", updatedUser.getProvider());
                            log.info("  - LastLogin: {}", updatedUser.getLastLoginDate());
                            log.info("=== OAuth2 로그인 완료 (기존 사용자) ===");

                            return updatedUser;
                        } else {
                            log.info("신규 사용자 등록 시작 - email: {}, provider: {}", user.getEmail(), registrationId);

                            user.updateLastLoginDate();
                            User savedUser = userRepository.save(user);

                            // 신규 사용자에게 기본 카테고리 생성
                            try {
                                categoryService.createDefaultCategories(savedUser.getId());
                                log.info("신규 사용자 기본 카테고리 생성 완료 - userId: {}", savedUser.getId());
                            } catch (Exception e) {
                                log.error("기본 카테고리 생성 실패 - userId: {}", savedUser.getId(), e);
                                // 기본 카테고리 생성 실패해도 로그인은 진행
                            }

                            log.info("신규 사용자 등록 완료");
                            log.info("  - UserId: {}", savedUser.getId());
                            log.info("  - Email: {}", savedUser.getEmail());
                            log.info("  - Provider: {}", savedUser.getProvider());
                            log.info("  - CreatedAt: {}", savedUser.getCreatedAt());
                            log.info("=== OAuth2 로그인 완료 (신규 사용자) ===");

                            return savedUser;
                        }
                    } catch (Exception e) {
                        log.error("사용자 저장/업데이트 실패 - email: {}, provider: {}", user.getEmail(), registrationId, e);
                        throw new OAuth2AuthenticationException("사용자 정보 저장 중 오류가 발생했습니다: " + e.getMessage());
                    }
                }
            }

            log.error("OAuth 프로세서를 찾을 수 없음");
            log.error("  - Provider: {}", registrationId);
            log.error("  - 등록된 프로세서 수: {}", oauthUserProcessors.size());

            throw new OAuth2AuthenticationException("지원하지 않는 OAuth 제공자입니다: " + registrationId);

        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth2 인증 실패", e);
            throw e;
        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 예상치 못한 오류 발생", e);
            throw new OAuth2AuthenticationException("로그인 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
