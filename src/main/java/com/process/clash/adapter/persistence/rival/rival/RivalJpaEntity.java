package com.process.clash.adapter.persistence.rival.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rivals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RivalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_first_user_id", nullable = false)
    private UserJpaEntity firstUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_second_user_id", nullable = false)
    private UserJpaEntity secondUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RivalLinkingStatus rivalLinkingStatus;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}