package com.process.clash.domain.roadmap.entity;

import com.process.clash.domain.common.enums.Major;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Section {

    private Long id;

    private Major major;

    private String title;

    private String description;

    private Category category;

    private Integer orderIndex;

    private List<Chapter> chapters;

    private List<SectionKeyPoint> keyPoints;

    private Set<Section> prerequisites = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 편의 메서드: 선수 과목 추가
    public void addPrerequisite(Section prerequisiteSection) {
        this.prerequisites.add(prerequisiteSection);
    }

    public Section update(String title, Category category, String description, Integer orderIndex) {
        if (title != null) {
            this.title = title;
        }
        if (category != null) {
            this.category = category;
        }
        if (description != null) {
            this.description = description;
        }
        if (orderIndex != null) {
            this.orderIndex = orderIndex;
        }
        return this;
    }

    public void updateOrderIndex(Integer newOrderIndex) {
        this.orderIndex = newOrderIndex;
    }
}