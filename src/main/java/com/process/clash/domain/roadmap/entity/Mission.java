package com.process.clash.domain.roadmap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Mission {

    private Long id;

    private Long chapterId;

    private String title;

    private Integer difficulty;

    private List<MissionQuestion> questions;
}