package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters_v2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChapterV2JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_section_id", nullable = false)
    private SectionJpaEntity section;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(length = 500)
    private String studyMaterialUrl;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionV2JpaEntity> questions = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
