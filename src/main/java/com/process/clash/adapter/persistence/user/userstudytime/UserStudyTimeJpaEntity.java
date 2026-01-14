package com.process.clash.adapter.persistence.user.userstudytime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    private Date date;

    @Column(nullable = false)
    private Long userId;

    public UserStudyTimeJpaEntity(Long id, Date date, Long userId) {
        this.id = id;
        this.date = date;
        this.userId = userId;
    }
}
