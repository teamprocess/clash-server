package com.process.clash.adapter.persistence.roadmap.choice;

import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "choices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChoiceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_question_id")
    private MissionQuestionJpaEntity question;

    @Column(nullable = false)
    private String content;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

}