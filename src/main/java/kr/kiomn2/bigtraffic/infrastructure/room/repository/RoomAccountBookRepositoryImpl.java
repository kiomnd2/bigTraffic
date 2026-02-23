package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomAccountBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomAccountBookRepositoryImpl implements RoomAccountBookRepository {

    private final RoomAccountBookJpaRepository jpaRepository;

    @Override
    public RoomAccountBook save(RoomAccountBook roomAccountBook) {
        return jpaRepository.save(roomAccountBook);
    }

    @Override
    public Optional<RoomAccountBook> findByRoomIdAndAccountBookId(Long roomId, Long accountBookId) {
        return jpaRepository.findByRoomIdAndAccountBookId(roomId, accountBookId);
    }

    @Override
    public List<RoomAccountBook> findByRoomId(Long roomId) {
        return jpaRepository.findByRoomId(roomId);
    }

    @Override
    public boolean existsByRoomIdAndAccountBookId(Long roomId, Long accountBookId) {
        return jpaRepository.existsByRoomIdAndAccountBookId(roomId, accountBookId);
    }

    @Override
    public void delete(RoomAccountBook roomAccountBook) {
        jpaRepository.delete(roomAccountBook);
    }

    @Override
    public void deleteAllByRoomId(Long roomId) {
        jpaRepository.deleteAllByRoomId(roomId);
    }
}
