package kr.kiomn2.bigtraffic.domain.room.service;

import kr.kiomn2.bigtraffic.domain.room.command.*;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.exception.*;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomMembersQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomQuery;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomsQuery;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomAccountBookRepository;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomInviteRepository;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomMemberRepository;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomInviteRepository roomInviteRepository;
    private final RoomAccountBookRepository roomAccountBookRepository;

    @Transactional
    public Room createRoom(CreateRoomCommand command) {
        Room room = Room.create(command);
        Room savedRoom = roomRepository.save(room);

        RoomMember host = RoomMember.createHost(savedRoom.getId(), command.getUserId());
        roomMemberRepository.save(host);

        return savedRoom;
    }

    public Room getRoom(GetRoomQuery query) {
        Room room = getRoomById(query.getRoomId());
        validateRoomMember(room.getId(), query.getUserId());
        return room;
    }

    public List<Room> getRooms(GetRoomsQuery query) {
        return roomRepository.findByUserId(query.getUserId());
    }

    public List<RoomMember> getRoomMembers(GetRoomMembersQuery query) {
        validateRoomMember(query.getRoomId(), query.getUserId());
        return roomMemberRepository.findByRoomId(query.getRoomId());
    }

    @Transactional
    public Room updateRoom(UpdateRoomCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);
        validateHost(room, command.getUserId());
        room.update(command);
        return room;
    }

    @Transactional
    public void dissolveRoom(DissolveRoomCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);
        validateHost(room, command.getUserId());

        room.dissolve();
        roomMemberRepository.deleteAllByRoomId(room.getId());
        roomAccountBookRepository.deleteAllByRoomId(room.getId());
    }

    @Transactional
    public RoomMember leaveRoom(LeaveRoomCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);

        RoomMember member = roomMemberRepository.findByRoomIdAndUserId(command.getRoomId(), command.getUserId())
                .orElseThrow(RoomMemberNotFoundException::new);

        if (member.isHost()) {
            throw new CannotLeaveAsHostException();
        }

        roomMemberRepository.delete(member);
        return member;
    }

    @Transactional
    public void kickMember(KickMemberCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);
        validateHost(room, command.getUserId());

        RoomMember targetMember = roomMemberRepository.findByRoomIdAndUserId(command.getRoomId(), command.getTargetUserId())
                .orElseThrow(RoomMemberNotFoundException::new);

        roomMemberRepository.delete(targetMember);
    }

    private Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

    private void validateActiveRoom(Room room) {
        if (!room.isActive()) {
            throw new RoomAlreadyDissolvedException();
        }
    }

    private void validateHost(Room room, Long userId) {
        if (!room.getHostUserId().equals(userId)) {
            throw new RoomHostPermissionException();
        }
    }

    private void validateRoomMember(Long roomId, Long userId) {
        if (!roomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new RoomMemberNotFoundException();
        }
    }
}
