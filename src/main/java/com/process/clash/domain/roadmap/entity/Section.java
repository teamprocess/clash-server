package com.process.clash.domain.roadmap.entity;

import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.domain.common.enums.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;
import java.util.List;
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

    private List<Chapter> chapters;

    private List<SectionKeyPoint> keyPoints;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
}