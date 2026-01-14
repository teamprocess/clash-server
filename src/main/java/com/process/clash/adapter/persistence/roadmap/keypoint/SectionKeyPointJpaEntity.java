package com.process.clash.adapter.persistence.roadmap.keypoint;

import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "section_key_points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SectionKeyPointJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_section_id", nullable = false)
    private SectionJpaEntity section;

    private String content;

    @Column(nullable = false)
    private Integer orderIndex;

}
