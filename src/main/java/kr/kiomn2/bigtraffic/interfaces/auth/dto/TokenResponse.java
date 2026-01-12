package kr.kiomn2.bigtraffic.interfaces.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String email;
    private String username;
    private String provider;

    public static TokenResponse of(String accessToken, Long expiresIn, String email, String username, String provider) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .email(email)
                .username(username)
                .provider(provider)
                .build();
    }
}
