package kr.kiomn2.bigtraffic.domain.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository {

    RoomMember save(RoomMember roomMember);

    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);

    List<RoomMember> findByRoomId(Long roomId);

    List<RoomMember> findByUserId(Long userId);

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    long countByRoomId(Long roomId);

    void delete(RoomMember roomMember);

    void deleteAllByRoomId(Long roomId);
}
