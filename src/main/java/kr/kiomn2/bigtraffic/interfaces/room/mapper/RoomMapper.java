package kr.kiomn2.bigtraffic.interfaces.room.mapper;

import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.interfaces.room.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {

    public RoomResponse toResponse(Room room) {
        return RoomResponse.from(room);
    }

    public RoomListResponse toListResponse(List<Room> rooms) {
        List<RoomResponse> roomResponses = rooms.stream()
                .map(RoomResponse::from)
                .toList();

        return RoomListResponse.builder()
                .rooms(roomResponses)
                .totalCount(roomResponses.size())
                .build();
    }

    public RoomMemberResponse toMemberResponse(RoomMember member) {
        return RoomMemberResponse.from(member);
    }

    public List<RoomMemberResponse> toMemberResponses(List<RoomMember> members) {
        return members.stream()
                .map(RoomMemberResponse::from)
                .toList();
    }

    public RoomInviteResponse toInviteResponse(RoomInvite invite) {
        return RoomInviteResponse.from(invite);
    }

    public List<RoomInviteResponse> toInviteResponses(List<RoomInvite> invites) {
        return invites.stream()
                .map(RoomInviteResponse::from)
                .toList();
    }

    public RoomAccountBookResponse toAccountBookResponse(RoomAccountBook roomAccountBook) {
        return RoomAccountBookResponse.from(roomAccountBook);
    }

    public List<RoomAccountBookResponse> toAccountBookResponses(List<RoomAccountBook> roomAccountBooks) {
        return roomAccountBooks.stream()
                .map(RoomAccountBookResponse::from)
                .toList();
    }
}
