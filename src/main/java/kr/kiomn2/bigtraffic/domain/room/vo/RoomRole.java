package kr.kiomn2.bigtraffic.domain.room.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomRole {
    HOST("방장"),
    MEMBER("멤버");

    private final String description;
}
