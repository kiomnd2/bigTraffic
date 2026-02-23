package kr.kiomn2.bigtraffic.interfaces.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RoomListResponse {

    private List<RoomResponse> rooms;
    private int totalCount;
}
