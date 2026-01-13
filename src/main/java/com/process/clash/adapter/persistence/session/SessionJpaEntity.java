package com.process.clash.adapter.persistence.session;

import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SessionJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private Boolean ableToAddRival;

    @Column
    private String profileImage;

    @Column(nullable = false)
    private Boolean pomodoroEnabled;

    @Column(nullable = false)
    private Integer pomodoroStudyMinute;

    @Column(nullable = false)
    private Integer pomodoroBreakMinute;

    @Enumerated(EnumType.STRING)
    private Major major;

    public SessionJpaEntity create(String username, String name, String password) {
        SessionJpaEntity sessionJpaEntity = new SessionJpaEntity();

        sessionJpaEntity.username = username;
        sessionJpaEntity.name = name;
        sessionJpaEntity.password = password;
        sessionJpaEntity.ableToAddRival = true;
        sessionJpaEntity.profileImage = null;
        sessionJpaEntity.pomodoroEnabled = false;
        sessionJpaEntity.pomodoroBreakMinute = 0;
        sessionJpaEntity.pomodoroStudyMinute = 0;
        sessionJpaEntity.major = null;

        return sessionJpaEntity;
    }
}