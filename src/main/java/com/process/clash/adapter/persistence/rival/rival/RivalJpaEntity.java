package com.process.clash.adapter.persistence.rival.rival;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import jakarta.persistence.*;
import jakarta.persistence.EntityListeners;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}