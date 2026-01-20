package com.process.clash.adapter.persistence.user.userpomodorosetting;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_pomodoro_setting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserPomodoroSettingJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean pomodoroEnabled;

    @Column(nullable = false)
    private int pomodoroStudyMinute;

    @Column(nullable = false)
    private int pomodoroBreakMinute;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;
}
