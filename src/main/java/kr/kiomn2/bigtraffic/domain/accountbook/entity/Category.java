package kr.kiomn2.bigtraffic.domain.accountbook.entity;

import jakarta.persistence.*;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 가계부 카테고리 엔티티
 * 수입/지출의 항목을 분류하며 통계에 활용
 */
@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TransactionType type;

    @Column(name = "color", length = 7)
    private String color;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateInfo(String name, String color, String icon) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        if (icon != null) {
            this.icon = icon;
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public static Category createDefault(Long userId, String name, TransactionType type, String color) {
        return Category.builder()
                .userId(userId)
                .name(name)
                .type(type)
                .color(color)
                .isDefault(true)
                .isActive(true)
                .build();
    }

    public static Category createCustom(Long userId, String name, TransactionType type, String color, String icon) {
        return Category.builder()
                .userId(userId)
                .name(name)
                .type(type)
                .color(color)
                .icon(icon)
                .isDefault(false)
                .isActive(true)
                .build();
    }
}
