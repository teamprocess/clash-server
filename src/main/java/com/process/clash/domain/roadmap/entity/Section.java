package com.process.clash.domain.roadmap.entity;

import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.domain.common.enums.Major;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "section_prerequisites",
            joinColumns = @JoinColumn(name = "section_id"),       // 현재 섹션
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id") // 선수 섹션
    )
    private Set<Section> prerequisites = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 편의 메서드: 선수 과목 추가
    public void addPrerequisite(Section prerequisiteSection) {
        this.prerequisites.add(prerequisiteSection);
    }

    public Section update(UpdateSectionData.Command command) {
        if (command.title() != null) {
            this.title = command.title();
        }
        if (command.category() != null) {
            this.category = command.category();
        }
        if (command.description() != null) {
            this.description = command.description();
        }
        if (command.orderIndex() != null) {
            this.orderIndex = command.orderIndex();
        }
        if (command.keyPoints() != null) {
            // keyPoints를 SectionKeyPoint 엔티티 리스트로 변환
            this.keyPoints = IntStream.range(0, command.keyPoints().size())
                    .mapToObj(index -> new SectionKeyPoint(
                            null,                           // id는 JPA가 자동 생성 (새 keyPoint인 경우)
                            this.id,                        // sectionId는 현재 Section의 id
                            command.keyPoints().get(index), // content
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