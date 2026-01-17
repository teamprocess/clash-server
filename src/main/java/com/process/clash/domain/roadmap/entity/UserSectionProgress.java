package com.process.clash.domain.roadmap.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSectionProgress {

    private Long id;

    private Long userId;

    private Long sectionId;

    private Long currentChapterId;

    private Integer completedChapters;

    private Boolean isCompleted;
}
