package kr.kiomn2.bigtraffic.application.room.facade;

import kr.kiomn2.bigtraffic.domain.room.command.*;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomInvitesQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomMembersQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomsQuery;
import kr.kiomn2.bigtraffic.domain.room.service.RoomAccountBookService;
import kr.kiomn2.bigtraffic.domain.room.service.RoomInviteService;
import kr.kiomn2.bigtraffic.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomFacade {

    private final RoomService roomService;
    private final RoomInviteService roomInviteService;
    private final RoomAccountBookService roomAccountBookService;

    @Transactional
    public Room createRoom(CreateRoomCommand command) {
        return roomService.createRoom(command);
    }

    public Room getRoom(GetRoomQuery query) {
        return roomService.getRoom(query);
    }

    public List<Room> getRooms(GetRoomsQuery query) {
        return roomService.getRooms(query);
    }

    @Transactional
    public Room updateRoom(UpdateRoomCommand command) {
        return roomService.updateRoom(command);
    }

    @Transactional
    public void dissolveRoom(DissolveRoomCommand command) {
        roomService.dissolveRoom(command);
    }

    public List<RoomMember> getRoomMembers(GetRoomMembersQuery query) {
        return roomService.getRoomMembers(query);
    }

    @Transactional
    public RoomMember joinRoom(JoinRoomCommand command) {
        return roomInviteService.joinRoom(command);
    }

    @Transactional
    public void leaveRoom(LeaveRoomCommand command) {
        roomService.leaveRoom(command);
    }

    @Transactional
    public void kickMember(KickMemberCommand command) {
        roomService.kickMember(command);
    }

    @Transactional
    public RoomInvite createInvite(CreateInviteCommand command) {
        return roomInviteService.createInvite(command);
    }

    public List<RoomInvite> getRoomInvites(GetRoomInvitesQuery query) {
        return roomInviteService.getRoomInvites(query);
    }

    @Transactional
    public void revokeInvite(RevokeInviteCommand command) {
        roomInviteService.revokeInvite(command);
    }

    @Transactional
    public RoomAccountBook linkAccountBook(LinkAccountBookCommand command) {
        return roomAccountBookService.linkAccountBook(command);
    }

    @Transactional
    public void unlinkAccountBook(UnlinkAccountBookCommand command) {
        roomAccountBookService.unlinkAccountBook(command);
    }

    public List<RoomAccountBook> getRoomAccountBooks(Long roomId, Long userId) {
        return roomAccountBookService.getRoomAccountBooks(roomId, userId);
    }
}
