package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomInvite;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomInviteRepositoryImpl implements RoomInviteRepository {

    private final RoomInviteJpaRepository jpaRepository;

    @Override
    public RoomInvite save(RoomInvite roomInvite) {
        return jpaRepository.save(roomInvite);
    }

    @Override
    public Optional<RoomInvite> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<RoomInvite> findByInviteCode(String inviteCode) {
        return jpaRepository.findByInviteCode(inviteCode);
    }

    @Override
    public List<RoomInvite> findByRoomId(Long roomId) {
        return jpaRepository.findByRoomId(roomId);
    }

    @Override
    public void delete(RoomInvite roomInvite) {
        jpaRepository.delete(roomInvite);
    }
}
