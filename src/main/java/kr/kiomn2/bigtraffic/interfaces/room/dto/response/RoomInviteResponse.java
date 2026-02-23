package kr.kiomn2.bigtraffic.interfaces.room.dto.response;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.vo.InviteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RoomInviteResponse {

    private Long id;
    private Long roomId;
    private String inviteCode;
    private Long createdBy;
    private InviteStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public static RoomInviteResponse from(RoomInvite invite) {
        return RoomInviteResponse.builder()
                .id(invite.getId())
                .roomId(invite.getRoomId())
                .inviteCode(invite.getInviteCode())
                .createdBy(invite.getCreatedBy())
                .status(invite.getStatus())
                .expiresAt(invite.getExpiresAt())
                .createdAt(invite.getCreatedAt())
                .build();
    }
}
