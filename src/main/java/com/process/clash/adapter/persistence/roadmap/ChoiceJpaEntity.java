package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    public void setQuestion(MissionQuestionJpaEntity question) {
        this.question = question;
    }

    public static ChoiceJpaEntity ofId(Long id) {
        ChoiceJpaEntity e = new ChoiceJpaEntity();
        try {
            java.lang.reflect.Field f = ChoiceJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {}
        return e;
    }

    public void setContent(String content) { this.content = content; }
    public void setIsCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }
}