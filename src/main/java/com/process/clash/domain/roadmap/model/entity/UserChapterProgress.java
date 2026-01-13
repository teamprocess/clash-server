package com.process.clash.domain.roadmap.model.entity;

import com.process.clash.domain.roadmap.model.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserChapterProgress {

    private Long id;

    private Long userId;

    private Long chapterId;

    private ProgressStatus status;
}