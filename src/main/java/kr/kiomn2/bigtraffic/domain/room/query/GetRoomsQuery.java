package kr.kiomn2.bigtraffic.domain.room.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRoomsQuery {
    private final Long userId;
}
