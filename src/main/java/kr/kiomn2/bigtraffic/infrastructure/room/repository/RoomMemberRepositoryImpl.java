package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomMember;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomMemberRepositoryImpl implements RoomMemberRepository {

    private final RoomMemberJpaRepository jpaRepository;

    @Override
    public RoomMember save(RoomMember roomMember) {
        return jpaRepository.save(roomMember);
    }

    @Override
    public Optional<RoomMember> findByRoomIdAndUserId(Long roomId, Long userId) {
        return jpaRepository.findByRoomIdAndUserId(roomId, userId);
    }

    @Override
    public List<RoomMember> findByRoomId(Long roomId) {
        return jpaRepository.findByRoomId(roomId);
    }

    @Override
    public List<RoomMember> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByRoomIdAndUserId(Long roomId, Long userId) {
        return jpaRepository.existsByRoomIdAndUserId(roomId, userId);
    }

    @Override
    public long countByRoomId(Long roomId) {
        return jpaRepository.countByRoomId(roomId);
    }

    @Override
    public void delete(RoomMember roomMember) {
        jpaRepository.delete(roomMember);
    }

    @Override
    public void deleteAllByRoomId(Long roomId) {
        jpaRepository.deleteAllByRoomId(roomId);
    }
}
