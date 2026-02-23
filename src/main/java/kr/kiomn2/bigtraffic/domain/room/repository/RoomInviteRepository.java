package kr.kiomn2.bigtraffic.domain.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;

import java.util.List;
import java.util.Optional;

public interface RoomInviteRepository {

    RoomInvite save(RoomInvite roomInvite);

    Optional<RoomInvite> findById(Long id);

    Optional<RoomInvite> findByInviteCode(String inviteCode);

    List<RoomInvite> findByRoomId(Long roomId);

    void delete(RoomInvite roomInvite);
}
