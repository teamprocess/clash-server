package com.process.clash.adapter.persistence.github;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GitHubDailyStatsId implements Serializable {
    private Long userId;
    private LocalDate studyDate;
}
