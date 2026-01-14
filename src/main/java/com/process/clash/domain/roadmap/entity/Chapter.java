package com.process.clash.domain.roadmap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chapter {

    private Long id;

    private Long sectionId;

    private String title;

    private String description;

    private Integer orderIndex;

    private List<Mission> missions;
}