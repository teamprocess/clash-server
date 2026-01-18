package com.process.clash.domain.roadmap.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserMissionHistory {

    private Long id;

    private Long userId;

    private Long missionId;

    private boolean isCleared;

    private Integer score;

    private Integer currentQuestionIndex;
}