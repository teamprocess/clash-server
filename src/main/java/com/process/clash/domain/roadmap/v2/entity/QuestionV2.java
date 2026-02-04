package com.process.clash.domain.roadmap.v2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuestionV2 {

    private Long id;

    private Long chapterId;

    private String content;

    private String explanation;

    private Integer orderIndex;

    private Integer difficulty;

    private List<ChoiceV2> choices;
}
