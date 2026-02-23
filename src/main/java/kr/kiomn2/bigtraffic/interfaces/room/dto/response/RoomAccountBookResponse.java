package kr.kiomn2.bigtraffic.interfaces.room.dto.response;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RoomAccountBookResponse {

    private Long id;
    private Long roomId;
    private Long accountBookId;
    private Long linkedBy;
    private LocalDateTime linkedAt;

    public static RoomAccountBookResponse from(RoomAccountBook roomAccountBook) {
        return RoomAccountBookResponse.builder()
                .id(roomAccountBook.getId())
                .roomId(roomAccountBook.getRoomId())
                .accountBookId(roomAccountBook.getAccountBookId())
                .linkedBy(roomAccountBook.getLinkedBy())
                .linkedAt(roomAccountBook.getLinkedAt())
                .build();
    }
}
