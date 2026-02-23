package kr.kiomn2.bigtraffic.domain.room.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.domain.room.vo.InviteStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_invites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoomInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "invite_code", nullable = false, unique = true, length = 36)
    private String inviteCode;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private InviteStatus status = InviteStatus.ACTIVE;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void revoke() {
        this.status = InviteStatus.REVOKED;
    }

    public boolean isValid() {
        return this.status == InviteStatus.ACTIVE && this.expiresAt.isAfter(LocalDateTime.now());
    }

    public static RoomInvite create(Long roomId, Long createdBy, int expirationHours) {
        return RoomInvite.builder()
                .roomId(roomId)
                .inviteCode(UUID.randomUUID().toString())
                .createdBy(createdBy)
                .status(InviteStatus.ACTIVE)
                .expiresAt(LocalDateTime.now().plusHours(expirationHours))
                .build();
    }
}
