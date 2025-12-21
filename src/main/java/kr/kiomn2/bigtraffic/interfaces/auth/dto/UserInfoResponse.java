package kr.kiomn2.bigtraffic.interfaces.auth.dto;

import kr.kiomn2.bigtraffic.domain.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String email;
    private String username;
    private String provider;
    private String providerId;
    private String profileUrl;
    private LocalDateTime lastLoginDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getProvider(),
                user.getProviderId(),
                user.getProfileUrl(),
                user.getLastLoginDate(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
