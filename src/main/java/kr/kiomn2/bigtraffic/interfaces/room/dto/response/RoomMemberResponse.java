package kr.kiomn2.bigtraffic.interfaces.room.dto.response;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.vo.RoomRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RoomMemberResponse {

    private Long id;
    private Long roomId;
    private Long userId;
    private RoomRole role;
    private LocalDateTime joinedAt;

    public static RoomMemberResponse from(RoomMember member) {
        return RoomMemberResponse.builder()
                .id(member.getId())
                .roomId(member.getRoomId())
                .userId(member.getUserId())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
