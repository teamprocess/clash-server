package com.process.clash.domain.roadmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SectionKeyPoint {

    private Long id;

    private Long sectionId;

    private String content;

    private Integer orderIndex;
}