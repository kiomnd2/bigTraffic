package kr.kiomn2.bigtraffic.infrastructure.room.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.kiomn2.bigtraffic.domain.room.entity.Room;
import kr.kiomn2.bigtraffic.domain.room.repository.RoomRepository;
import kr.kiomn2.bigtraffic.domain.room.vo.RoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.kiomn2.bigtraffic.domain.room.entity.QRoom.room;
import static kr.kiomn2.bigtraffic.domain.room.entity.QRoomMember.roomMember;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Room save(Room entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<Room> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Room> findByUserId(Long userId) {
        return queryFactory
                .selectFrom(room)
                .join(roomMember).on(roomMember.roomId.eq(room.id))
                .where(
                        roomMember.userId.eq(userId),
                        room.status.eq(RoomStatus.ACTIVE)
                )
                .orderBy(room.createdAt.desc())
                .fetch();
    }

    @Override
    public void delete(Room entity) {
        jpaRepository.delete(entity);
    }
}
