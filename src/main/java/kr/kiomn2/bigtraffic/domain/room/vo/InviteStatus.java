package kr.kiomn2.bigtraffic.domain.room.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InviteStatus {
    ACTIVE("활성"),
    EXPIRED("만료"),
    REVOKED("취소");

    private final String description;
}
