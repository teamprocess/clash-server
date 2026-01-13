package com.process.clash.domain.roadmap.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Choice {

    private Long id;

    private Long questionId;

    private String content;

    private boolean isCorrect;
}