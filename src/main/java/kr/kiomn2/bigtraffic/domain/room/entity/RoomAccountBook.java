package kr.kiomn2.bigtraffic.domain.room.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_account_books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"room_id", "account_book_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomAccountBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "account_book_id", nullable = false)
    private Long accountBookId;

    @Column(name = "linked_by", nullable = false)
    private Long linkedBy;

    @CreationTimestamp
    @Column(name = "linked_at", nullable = false, updatable = false)
    private LocalDateTime linkedAt;

    public static RoomAccountBook create(Long roomId, Long accountBookId, Long linkedBy) {
        return RoomAccountBook.builder()
                .roomId(roomId)
                .accountBookId(accountBookId)
                .linkedBy(linkedBy)
                .build();
    }
}
