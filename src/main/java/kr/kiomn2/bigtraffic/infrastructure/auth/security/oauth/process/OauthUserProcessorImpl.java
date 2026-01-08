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
        try {
            log.info("카카오 OAuth2 사용자 정보 처리 시작");

            // 필수 속성 추출 및 검증
            Long kakaoId = oAuth2User.getAttribute("id");
            if (kakaoId == null) {
                log.error("카카오 ID를 찾을 수 없습니다. OAuth2User attributes: {}", oAuth2User.getAttributes());
                throw new IllegalArgumentException("카카오 ID를 찾을 수 없습니다.");
            }

            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount == null) {
                log.error("카카오 계정 정보를 찾을 수 없습니다. KakaoId: {}", kakaoId);
                throw new IllegalArgumentException("카카오 계정 정보를 찾을 수 없습니다.");
            }

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile == null) {
                log.error("카카오 프로필 정보를 찾을 수 없습니다. KakaoId: {}", kakaoId);
                throw new IllegalArgumentException("카카오 프로필 정보를 찾을 수 없습니다.");
            }

            String email = (String) kakaoAccount.get("email");
            if (email == null || email.isBlank()) {
                log.error("카카오 이메일 정보를 찾을 수 없습니다. KakaoId: {}", kakaoId);
                throw new IllegalArgumentException("카카오 이메일 동의가 필요합니다.");
            }

            String nickname = (String) profile.get("nickname");
            if (nickname == null || nickname.isBlank()) {
                log.warn("카카오 닉네임이 없습니다. 기본 닉네임을 사용합니다. KakaoId: {}", kakaoId);
                nickname = "사용자" + kakaoId;
            }

            String profileImage = (String) profile.get("profile_image_url");

            log.info("카카오 사용자 정보 추출 완료 - ID: {}, Email: {}, Nickname: {}", kakaoId, email, nickname);

            // User 엔티티 생성
            return User.builder()
                    .email(email)
                    .username(nickname)
                    .provider(provider)
                    .providerId(String.valueOf(kakaoId))
                    .profileUrl(profileImage)
                    .build();

        } catch (ClassCastException e) {
            log.error("카카오 사용자 정보 형식이 올바르지 않습니다.", e);
            log.error("OAuth2User attributes: {}", oAuth2User.getAttributes());
            throw new IllegalArgumentException("카카오 사용자 정보 형식이 올바르지 않습니다.", e);
        } catch (Exception e) {
            log.error("카카오 사용자 정보 처리 중 예외 발생", e);
            log.error("OAuth2User attributes: {}", oAuth2User.getAttributes());
            throw new IllegalArgumentException("카카오 로그인 처리 중 오류가 발생했습니다.", e);
        }
    }
}
