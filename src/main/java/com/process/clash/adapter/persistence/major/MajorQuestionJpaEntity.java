package com.process.clash.adapter.persistence.major;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "major_questions")
public class MajorQuestionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; // 질문 텍스트 (예: "논리적인 문제 해결을 즐긴다")

    @Embedded
    private WeightVo weightVo;

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
