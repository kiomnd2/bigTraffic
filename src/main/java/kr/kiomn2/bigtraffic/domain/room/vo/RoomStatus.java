package kr.kiomn2.bigtraffic.domain.room.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomStatus {
    ACTIVE("활성"),
    DISSOLVED("해산");

    private final String description;
}
