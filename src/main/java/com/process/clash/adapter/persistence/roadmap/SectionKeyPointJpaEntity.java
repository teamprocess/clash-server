package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;

@Entity
@Table(name = "section_key_points")
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