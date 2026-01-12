package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChapterJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_section_id")
    private SectionJpaEntity section;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Integer orderIndex; // 순서

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionJpaEntity> missions = new ArrayList<>();

    public void setSection(SectionJpaEntity section) {
        this.section = section;
    }

    public void setMissions(List<MissionJpaEntity> missions) {
        this.missions = missions;
    }

    public static ChapterJpaEntity ofId(Long id) {
        ChapterJpaEntity e = new ChapterJpaEntity();
        try {
            java.lang.reflect.Field f = ChapterJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {
            // ignore
        }
        return e;
    }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}