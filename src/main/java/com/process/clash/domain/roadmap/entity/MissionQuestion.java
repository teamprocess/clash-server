package com.process.clash.domain.roadmap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MissionQuestion {

    private Long id;

    private Long missionId;

    private String content;

    private String explanation;

    private List<Choice> choices;
}