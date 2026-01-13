package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.*;
import lombok.*;
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

}