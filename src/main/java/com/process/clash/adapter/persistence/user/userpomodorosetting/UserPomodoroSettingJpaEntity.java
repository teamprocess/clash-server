package com.process.clash.adapter.persistence.user.userpomodorosetting;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
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
@Table(name = "user_pomodoro_setting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserPomodoroSettingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean pomodoroEnabled;

    @Column(nullable = false)
    private int pomodoroStudyMinute;

    @Column(nullable = false)
    private int pomodoroBreakMinute;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false, unique = true)
    private UserJpaEntity user;
}
