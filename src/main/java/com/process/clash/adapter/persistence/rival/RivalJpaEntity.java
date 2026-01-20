package com.process.clash.adapter.persistence.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.enums.RivalCurrentStatus;
import com.process.clash.domain.rival.enums.RivalLinkingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "rivals",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_rival_pair",
                        columnNames = {"fk_my_id", "fk_opponent_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RivalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RivalLinkingStatus rivalLinkingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_my_id", nullable = false)
    private UserJpaEntity my;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_opponent_id", nullable = false)
    private UserJpaEntity opponent;
}
