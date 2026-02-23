package kr.kiomn2.bigtraffic.domain.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {

    Room save(Room room);

    Optional<Room> findById(Long id);

    List<Room> findByUserId(Long userId);

    void delete(Room room);
}
