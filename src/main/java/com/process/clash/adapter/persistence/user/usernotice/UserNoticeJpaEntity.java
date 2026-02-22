package com.process.clash.adapter.persistence.user.usernotice;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
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
@Table(name = "user_notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserNoticeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeCategory noticeCategory;

    @Column(nullable = false)
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_sender_id", nullable = false)
    private UserJpaEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_receiver_id", nullable = false)
    private UserJpaEntity receiver;

    @Column
    private Long rivalId;

    @Column
    private Long battleId;
}
