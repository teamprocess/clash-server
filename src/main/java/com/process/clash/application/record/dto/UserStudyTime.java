package com.process.clash.application.record.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserStudyTime {
    private Long userId;
    private Long totalSeconds;
}