package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission_questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MissionQuestionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_mission_id")
    private MissionJpaEntity mission;

    @Column(nullable = false, length = 1000)
    private String content;

    private String explanation; // 해설

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoiceJpaEntity> choices = new ArrayList<>();

    public void setMission(MissionJpaEntity mission) {
        this.mission = mission;
    }

    public void setChoices(List<ChoiceJpaEntity> choices) {
        this.choices = choices;
    }
    
        public static MissionQuestionJpaEntity ofId(Long id) {
            MissionQuestionJpaEntity e = new MissionQuestionJpaEntity();
            try {
                java.lang.reflect.Field f = MissionQuestionJpaEntity.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(e, id);
            } catch (Exception ex) {
            }
            return e;
        }
    
        public void setContent(String content) { this.content = content; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
}