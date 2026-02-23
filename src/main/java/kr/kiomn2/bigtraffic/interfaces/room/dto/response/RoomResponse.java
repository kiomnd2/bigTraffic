package kr.kiomn2.bigtraffic.interfaces.room.dto.response;

import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.vo.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RoomResponse {

    private Long id;
    private Long hostUserId;
    private String name;
    private String description;
    private Integer maxMembers;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .hostUserId(room.getHostUserId())
                .name(room.getName())
                .description(room.getDescription())
                .maxMembers(room.getMaxMembers())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}
