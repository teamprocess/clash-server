package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SectionJpaEntity { // roadmap

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;

    @Column(nullable = false)
    private String title;

    private String description;

    private String category;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChapterJpaEntity> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private List<SectionKeyPointJpaEntity> keyPoints = new ArrayList<>();

    public void setChapters(List<ChapterJpaEntity> chapters) {
        this.chapters = chapters;
    }

    public void setKeyPoints(List<SectionKeyPointJpaEntity> keyPoints) {
        this.keyPoints = keyPoints;
    }

    public static SectionJpaEntity ofId(Long id) {
        SectionJpaEntity e = new SectionJpaEntity();
        try {
            java.lang.reflect.Field f = SectionJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {
            // ignore
        }
        return e;
    }

    public void setMajor(com.process.clash.domain.common.enums.Major major) {
        this.major = major;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}