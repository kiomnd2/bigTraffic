package kr.kiomn2.bigtraffic.domain.room.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRoomMembersQuery {
    private final Long userId;
    private final Long roomId;
}
