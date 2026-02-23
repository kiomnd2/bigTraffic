package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomMemberJpaRepository extends JpaRepository<RoomMember, Long> {

    Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId);

    List<RoomMember> findByRoomId(Long roomId);

    List<RoomMember> findByUserId(Long userId);

    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    long countByRoomId(Long roomId);

    void deleteAllByRoomId(Long roomId);
}
