package com.process.clash.domain.roadmap.v2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChoiceV2 {

    private Long id;

    private Long questionId;

    private String content;

    private boolean isCorrect;

    private Integer orderIndex;
}
