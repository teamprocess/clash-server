package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "section_key_points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SectionKeyPointJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_section_id")
    private SectionJpaEntity section;

    private String content;

    @Column(nullable = false)
    private Integer orderIndex;
}