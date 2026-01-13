package com.process.clash.adapter.persistence.user;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserJpaEntity {

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

    @Column(nullable = false)
    private Role role;

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

    public UserJpaEntity create(String username, String name, String password) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();

        userJpaEntity.username = username;
        userJpaEntity.name = name;
        userJpaEntity.password = password;
        userJpaEntity.role = Role.USER;
        userJpaEntity.ableToAddRival = true;
        userJpaEntity.profileImage = null;
        userJpaEntity.pomodoroEnabled = false;
        userJpaEntity.pomodoroBreakMinute = 0;
        userJpaEntity.pomodoroStudyMinute = 0;
        userJpaEntity.major = null;

        return userJpaEntity;
    }
}