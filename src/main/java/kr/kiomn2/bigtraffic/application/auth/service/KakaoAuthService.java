package kr.kiomn2.bigtraffic.application.auth.service;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import kr.kiomn2.bigtraffic.infrastructure.auth.repository.UserRepository;
import kr.kiomn2.bigtraffic.infrastructure.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

/** ~
     *
     * 카카오 인가 코드로 액세스 토큰 받기
     */
    public String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            log.info("카카오 액세스 토큰 요청 - code: {}", code);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");
                log.info("카카오 액세스 토큰 발급 성공");
                return accessToken;
            } else {
                log.error("카카오 액세스 토큰 발급 실패 - HTTP Status: {}", response.getStatusCode());
                throw new RuntimeException("카카오 액세스 토큰 발급 실패");
            }
        } catch (Exception e) {
            log.error("카카오 액세스 토큰 요청 중 오류 발생", e);
            throw new RuntimeException("카카오 액세스 토큰 요청 실패", e);
        }
    }

    /**
     * 카카오 사용자 정보 가져오기
     */
    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            log.info("카카오 사용자 정보 요청");
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("카카오 사용자 정보 조회 성공");
                return response.getBody();
            } else {
                log.error("카카오 사용자 정보 조회 실패 - HTTP Status: {}", response.getStatusCode());
                throw new RuntimeException("카카오 사용자 정보 조회 실패");
            }
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 중 오류 발생", e);
            throw new RuntimeException("카카오 사용자 정보 조회 실패", e);
        }
    }

    /**
     * 카카오 로그인 처리 (사용자 저장 또는 조회 후 JWT 토큰 발급)
     */
    @Transactional
    public String processKakaoLogin(String code) {
        // 1. 인가 코드로 액세스 토큰 받기
        String kakaoAccessToken = getKakaoAccessToken(code);

        // 2. 액세스 토큰으로 사용자 정보 가져오기
        Map<String, Object> userInfo = getKakaoUserInfo(kakaoAccessToken);

        // 3. 사용자 정보 파싱
        Long kakaoId = ((Number) userInfo.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        log.info("카카오 로그인 - ID: {}, Email: {}, Nickname: {}", kakaoId, email, nickname);

        // 4. 사용자 저장 또는 조회
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("새로운 사용자 생성 - email: {}", email);
                    User newUser = User.builder()
                            .email(email)
                            .username(nickname)
                            .provider("kakao")
                            .providerId(String.valueOf(kakaoId))
                            .build();
                    return userRepository.save(newUser);
                });

        // 5. JWT 토큰 발급
        String jwtToken = jwtTokenProvider.createToken(user.getEmail());
        log.info("JWT 토큰 발급 완료 - email: {}", user.getEmail());

        return jwtToken;
    }
}
