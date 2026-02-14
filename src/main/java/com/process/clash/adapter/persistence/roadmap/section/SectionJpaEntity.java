package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import jakarta.persistence.EntityListeners;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SectionJpaEntity { // 로드맵

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;

    @Column(nullable = false)
    private Integer orderIndex;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<ChapterJpaEntity> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionKeyPointJpaEntity> keyPoints = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "section_prerequisites",
            joinColumns = @JoinColumn(name = "section_id"),       // 현재 섹션
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id") // 선수 섹션
    )
    private Set<SectionJpaEntity> prerequisites = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    public void updateFields(Major major, String title, CategoryJpaEntity category, String description, Integer orderIndex) {
        this.major = major;
        this.title = title;
        this.category = category;
        this.description = description;
        this.orderIndex = orderIndex;
    }

    public void updateChapters(List<ChapterJpaEntity> newChapters) {
        this.chapters.clear(); // 기존 리스트를 비우고
        this.chapters.addAll(newChapters); // 새 리스트를 채움 (고아 객체 자동 삭제됨)
    }

    public void updateKeyPoints(List<SectionKeyPointJpaEntity> newKeyPoints) {
        this.keyPoints.clear();
        this.keyPoints.addAll(newKeyPoints);
    }

    public void updatePrerequisites(Set<SectionJpaEntity> newPrerequisites) {
        this.prerequisites.clear();
        this.prerequisites.addAll(newPrerequisites);
    }

}