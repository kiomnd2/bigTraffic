package kr.kiomn2.bigtraffic.domain.room.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.domain.room.command.CreateRoomCommand;
import kr.kiomn2.bigtraffic.domain.room.command.UpdateRoomCommand;
import kr.kiomn2.bigtraffic.domain.room.vo.RoomStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "host_user_id", nullable = false)
    private Long hostUserId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "max_members", nullable = false)
    @Builder.Default
    private Integer maxMembers = 10;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private RoomStatus status = RoomStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(UpdateRoomCommand command) {
        if (command.getName() != null) {
            this.name = command.getName();
        }
        if (command.getDescription() != null) {
            this.description = command.getDescription();
        }
        if (command.getMaxMembers() != null) {
            this.maxMembers = command.getMaxMembers();
        }
    }

    public void dissolve() {
        this.status = RoomStatus.DISSOLVED;
    }

    public boolean isActive() {
        return this.status == RoomStatus.ACTIVE;
    }

    public static Room create(CreateRoomCommand command) {
        return Room.builder()
                .hostUserId(command.getUserId())
                .name(command.getName())
                .description(command.getDescription())
                .maxMembers(command.getMaxMembers() != null ? command.getMaxMembers() : 10)
                .status(RoomStatus.ACTIVE)
                .build();
    }
}
