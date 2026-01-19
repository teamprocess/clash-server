package com.process.clash.adapter.persistence.user.user;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean ableToAddRival;

    @Column
    private String profileImage;

    @Column(nullable = false)
    private boolean pomodoroEnabled;

    @Column(nullable = false)
    private Integer pomodoroStudyMinute;

    @Column(nullable = false)
    private Integer pomodoroBreakMinute;

    @Enumerated(EnumType.STRING)
    private Major major;
}