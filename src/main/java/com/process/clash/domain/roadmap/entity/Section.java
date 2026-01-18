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
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Section {

    private Long id;

    private Major major;

    private String title;

    private String description;

    private String category;

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

    public Section update(String title, String category, String description, Integer orderIndex, List<String> keyPoints) {
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
        if (keyPoints != null) {
            // keyPoints를 SectionKeyPoint 엔티티 리스트로 변환
            this.keyPoints = IntStream.range(0, keyPoints.size())
                    .mapToObj(index -> new SectionKeyPoint(
                            null,                           // id는 JPA가 자동 생성 (새 keyPoint인 경우)
                            this.id,                        // sectionId는 현재 Section의 id
                            keyPoints.get(index), // content
                            index                           // orderIndex
                    ))
                    .toList();
        }
        return this;
    }

    public void updateOrderIndex(Integer newOrderIndex) {
        this.orderIndex = newOrderIndex;
    }
}