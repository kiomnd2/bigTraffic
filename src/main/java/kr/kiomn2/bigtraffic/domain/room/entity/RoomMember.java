package kr.kiomn2.bigtraffic.domain.room.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.domain.room.vo.RoomRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"room_id", "user_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private RoomRole role;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public boolean isHost() {
        return this.role == RoomRole.HOST;
    }

    public static RoomMember createHost(Long roomId, Long userId) {
        return RoomMember.builder()
                .roomId(roomId)
                .userId(userId)
                .role(RoomRole.HOST)
                .build();
    }

    public static RoomMember createMember(Long roomId, Long userId) {
        return RoomMember.builder()
                .roomId(roomId)
                .userId(userId)
                .role(RoomRole.MEMBER)
                .build();
    }
}
