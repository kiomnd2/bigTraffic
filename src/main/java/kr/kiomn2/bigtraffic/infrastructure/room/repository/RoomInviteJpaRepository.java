package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomInviteJpaRepository extends JpaRepository<RoomInvite, Long> {

    Optional<RoomInvite> findByInviteCode(String inviteCode);

    List<RoomInvite> findByRoomId(Long roomId);
}
