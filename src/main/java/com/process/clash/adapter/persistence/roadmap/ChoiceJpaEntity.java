package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "choices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoiceJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_question_id")
    private QuestionJpaEntity question;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isCorrect;
}