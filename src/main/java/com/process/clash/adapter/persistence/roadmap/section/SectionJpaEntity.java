package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
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

    private String category;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}