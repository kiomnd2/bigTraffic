package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomJpaRepository extends JpaRepository<Room, Long> {
}
