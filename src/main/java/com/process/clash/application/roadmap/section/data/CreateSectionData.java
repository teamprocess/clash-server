package com.process.clash.application.roadmap.section.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Category;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

public class CreateSectionData {

    public record Command(
            Actor actor,
            Major major,
            String title,
            String category,
            String description,
            List<String> keyPoints
    ) {
        public Section toDomain(Integer orderIndex, Category category) {
            // keyPoints를 SectionKeyPoint 엔티티 리스트로 변환
            List<SectionKeyPoint> sectionKeyPoints = IntStream.range(0, keyPoints.size())
                    .mapToObj(index -> new SectionKeyPoint(
                            null,           // id는 JPA가 자동 생성
                            null,           // sectionId는 Section 저장 시 자동 설정 (cascade)
                            keyPoints.get(index),  // content
                            index           // orderIndex
                    ))
                    .toList();

            return new Section(null, major, title, description, category, orderIndex, List.of(), sectionKeyPoints, new HashSet<>(), null, null);
        }
    }

    public record Result(
            Long sectionId,
            String major,
            String title,
            String category,
            String description,
            List<String> keyPoints,
            String createdAt
    ) {
        public static Result from(Section section, List<String> keyPoints) {
            String createdAtString = section.getCreatedAt() != null
                    ? section.getCreatedAt().toString()
                    : null;

            return new Result(
                    section.getId(),
                    section.getMajor().name(),
                    section.getTitle(),
                    section.getCategory().getName(),
                    section.getDescription(),
                    keyPoints,
                    createdAtString
            );
        }
    }
}
