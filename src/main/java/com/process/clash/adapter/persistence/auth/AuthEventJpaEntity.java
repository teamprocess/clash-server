package com.process.clash.adapter.persistence.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "auth_events")
public class AuthEventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String eventType; // SESSION_EXPIRE, LOGIN or LOGOUT

    private String ipAddress;

    private String device;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    public AuthEventJpaEntity(String username, String eventType, String ipAddress, String device, LocalDateTime occurredAt) {
        this.username = username;
        this.eventType = eventType;
        this.ipAddress = ipAddress;
        this.device = device;
        this.occurredAt = occurredAt;
    }

}
