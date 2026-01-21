package com.process.clash.domain.roadmap.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Mission {

    private Long id;

    private Long chapterId;

    private String title;

    private Integer difficulty;

    private Integer orderIndex;

    private List<MissionQuestion> questions;
}