package kr.kiomn2.bigtraffic.domain.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.RoomAccountBook;

import java.util.List;
import java.util.Optional;

public interface RoomAccountBookRepository {

    RoomAccountBook save(RoomAccountBook roomAccountBook);

    Optional<RoomAccountBook> findByRoomIdAndAccountBookId(Long roomId, Long accountBookId);

    List<RoomAccountBook> findByRoomId(Long roomId);

    boolean existsByRoomIdAndAccountBookId(Long roomId, Long accountBookId);

    void delete(RoomAccountBook roomAccountBook);

    void deleteAllByRoomId(Long roomId);
}
