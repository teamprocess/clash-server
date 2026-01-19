package com.process.clash.adapter.persistence.user.userstudytime;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(
        name = "user_study_times",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"fk_user_id", "date"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStudyTimeJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private long totalStudyTimeSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    public UserStudyTimeJpaEntity(Long id, LocalDate date, long totalStudyTimeSeconds, UserJpaEntity user) {
        this.id = id;
        this.date = date;
        this.totalStudyTimeSeconds = totalStudyTimeSeconds;
        this.user = user;
    }
}
