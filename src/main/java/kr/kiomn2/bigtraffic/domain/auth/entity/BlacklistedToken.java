package kr.kiomn2.bigtraffic.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens", indexes = {
        @Index(name = "idx_token", columnList = "token"),
        @Index(name = "idx_expiration", columnList = "expiration_date")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @CreationTimestamp
    @Column(name = "blacklisted_at", nullable = false, updatable = false)
    private LocalDateTime blacklistedAt;

    @Column(length = 100)
    private String reason;
}
