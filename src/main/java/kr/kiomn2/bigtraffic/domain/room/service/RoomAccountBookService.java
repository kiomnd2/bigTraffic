package kr.kiomn2.bigtraffic.domain.room.service;

import kr.kiomn2.bigtraffic.domain.room.command.LinkAccountBookCommand;
import kr.kiomn2.bigtraffic.domain.room.command.UnlinkAccountBookCommand;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import kr.kiomn2.bigtraffic.domain.room.exception.*;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomAccountBookRepository;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomMemberRepository;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomAccountBookService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomAccountBookRepository roomAccountBookRepository;

    @Transactional
    public RoomAccountBook linkAccountBook(LinkAccountBookCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);
        validateRoomMember(command.getRoomId(), command.getUserId());

        if (roomAccountBookRepository.existsByRoomIdAndAccountBookId(command.getRoomId(), command.getAccountBookId())) {
            throw new RoomAccountBookAlreadyLinkedException();
        }

        RoomAccountBook roomAccountBook = RoomAccountBook.create(
                command.getRoomId(), command.getAccountBookId(), command.getUserId()
        );
        return roomAccountBookRepository.save(roomAccountBook);
    }

    @Transactional
    public void unlinkAccountBook(UnlinkAccountBookCommand command) {
        Room room = getRoomById(command.getRoomId());
        validateActiveRoom(room);
        validateHost(room, command.getUserId());

        RoomAccountBook roomAccountBook = roomAccountBookRepository
                .findByRoomIdAndAccountBookId(command.getRoomId(), command.getAccountBookId())
                .orElseThrow(RoomAccountBookNotFoundException::new);

        roomAccountBookRepository.delete(roomAccountBook);
    }

    public List<RoomAccountBook> getRoomAccountBooks(Long roomId, Long userId) {
        validateRoomMember(roomId, userId);
        return roomAccountBookRepository.findByRoomId(roomId);
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
