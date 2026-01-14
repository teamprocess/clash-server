package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "user_study_times")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStudyTimeJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    public UserStudyTimeJpaEntity(Long id, LocalDate date, UserJpaEntity user) {
        this.id = id;
        this.date = date;
        this.user = user;
    }
}
