package com.process.clash.domain.roadmap.v2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChapterV2 {

    private Long id;

    private Long sectionId;

    private String title;

    private String description;

    private Integer orderIndex;

    private String studyMaterialUrl;

    private List<QuestionV2> questions;
}
