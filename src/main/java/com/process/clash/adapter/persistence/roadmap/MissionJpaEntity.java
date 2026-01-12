package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MissionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_chapter_id")
    private ChapterJpaEntity chapter;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer difficulty;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionQuestionJpaEntity> questions = new ArrayList<>();

    public void setChapter(ChapterJpaEntity chapter) {
        this.chapter = chapter;
    }

    public void setQuestions(List<MissionQuestionJpaEntity> questions) {
        this.questions = questions;
    }

    public static MissionJpaEntity ofId(Long id) {
        MissionJpaEntity e = new MissionJpaEntity();
        try {
            java.lang.reflect.Field f = MissionJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {
        }
        return e;
    }

    public void setTitle(String title) { this.title = title; }
    public void setDifficulty(Integer difficulty) { this.difficulty = difficulty; }
}