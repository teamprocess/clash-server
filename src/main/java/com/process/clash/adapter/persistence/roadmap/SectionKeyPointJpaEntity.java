package com.process.clash.adapter.persistence.roadmap;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @JoinColumn(name = "fk_section_id")
    private SectionJpaEntity section;

    private String content;

    @Column(nullable = false)
    private Integer orderIndex;

    public void setSection(SectionJpaEntity section) {
        this.section = section;
    }

    public static SectionKeyPointJpaEntity ofId(Long id) {
        SectionKeyPointJpaEntity e = new SectionKeyPointJpaEntity();
        try {
            java.lang.reflect.Field f = SectionKeyPointJpaEntity.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(e, id);
        } catch (Exception ex) {}
        return e;
    }

    public void setContent(String content) { this.content = content; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}