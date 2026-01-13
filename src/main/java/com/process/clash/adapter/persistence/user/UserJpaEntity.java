package com.process.clash.adapter.persistence.user;

import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private boolean ableToAddRival;

    @Enumerated(EnumType.STRING)
    private Major major;

    public UserJpaEntity create(String username, String name, String password) {
        UserJpaEntity userJpaEntity = new UserJpaEntity();

        userJpaEntity.username = username;
        userJpaEntity.name = name;
        userJpaEntity.password = password;
        userJpaEntity.ableToAddRival = true;
        userJpaEntity.major = null;

        return userJpaEntity;
    }
}