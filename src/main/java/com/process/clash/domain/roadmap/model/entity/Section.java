package com.process.clash.domain.roadmap.model.entity;

import com.process.clash.domain.common.enums.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;

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
}