package com.process.clash.adapter.persistence.major;

import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "major_questions")
public class MajorQuestionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; // 질문 텍스트 (예: "논리적인 문제 해결을 즐긴다")

    @Embedded
    private WeightVo weightVo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class WeightVo {

        private Integer web;

        private Integer app;

        private Integer server;

        private Integer ai;

        private Integer game;
    }

}
