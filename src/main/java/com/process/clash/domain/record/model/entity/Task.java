package com.process.clash.domain.record.model.entity;

import com.process.clash.domain.record.model.enums.TaskColor;

public record Task(
    Long id,
    String name,
    String icon,
    TaskColor color,
    Long studyTime // ms
) {}
