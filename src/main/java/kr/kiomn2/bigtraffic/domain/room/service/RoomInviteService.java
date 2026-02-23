package kr.kiomn2.bigtraffic.domain.room.service;

import kr.kiomn2.bigtraffic.domain.room.command.CreateInviteCommand;
import kr.kiomn2.bigtraffic.domain.room.command.JoinRoomCommand;
import kr.kiomn2.bigtraffic.domain.room.command.RevokeInviteCommand;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.exception.*;
import kr.kiomn2.bigtraffic.domain.room.query.GetRoomInvitesQuery;
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
public class RoomInviteService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomInviteRepository roomInviteRepository;

    @Transactional
    public RoomInvite createInvite(CreateInviteCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);
        validateHost(room, command.getUserId());

        int expirationHours = command.getExpirationHours() != null ? command.getExpirationHours() : 24;
        RoomInvite invite = RoomInvite.create(room.getId(), command.getUserId(), expirationHours);
        return roomInviteRepository.save(invite);
    }

    @Transactional
    public RoomMember joinRoom(JoinRoomCommand command) {
        RoomInvite invite = roomInviteRepository.findByInviteCode(command.getInviteCode())
                .orElseThrow(InvalidInviteCodeException::new);

        if (!invite.isValid()) {
            throw new InvalidInviteCodeException("만료되거나 취소된 초대 코드입니다.");
        }

        Room room = getRoomById(invite.getRoomId());
        validateActiveRoom(room);

        if (roomMemberRepository.existsByRoomIdAndUserId(room.getId(), command.getUserId())) {
            throw new AlreadyRoomMemberException();
        }

        long currentMemberCount = roomMemberRepository.countByRoomId(room.getId());
        if (currentMemberCount >= room.getMaxMembers()) {
            throw new RoomMemberLimitExceededException();
        }

        RoomMember member = RoomMember.createMember(room.getId(), command.getUserId());
        return roomMemberRepository.save(member);
    }

    public List<RoomInvite> getRoomInvites(GetRoomInvitesQuery query) {
        Room room = getRoomById(query.getRoomId());
        validateHost(room, query.getUserId());
        return roomInviteRepository.findByRoomId(query.getRoomId());
    }

    @Transactional
    public void revokeInvite(RevokeInviteCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateHost(room, command.getUserId());

        RoomInvite invite = roomInviteRepository.findById(command.getInviteId())
                .orElseThrow(InvalidInviteCodeException::new);

        invite.revoke();
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
}
