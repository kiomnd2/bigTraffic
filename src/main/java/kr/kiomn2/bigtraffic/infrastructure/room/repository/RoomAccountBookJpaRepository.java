package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomAccountBookJpaRepository extends JpaRepository<RoomAccountBook, Long> {

    Optional<RoomAccountBook> findByRoomIdAndAccountBookId(Long roomId, Long accountBookId);

    List<RoomAccountBook> findByRoomId(Long roomId);

    boolean existsByRoomIdAndAccountBookId(Long roomId, Long accountBookId);

    void deleteAllByRoomId(Long roomId);
}
