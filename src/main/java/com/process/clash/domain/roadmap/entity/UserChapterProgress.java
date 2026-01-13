package com.process.clash.domain.roadmap.entity;

import com.process.clash.domain.roadmap.enums.ProgressStatus;
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