package com.process.clash.domain.user.userstudytime.entity;

import java.util.Date;

public record UserStudyTime(
        Long id,
        Date date,
        Long userId
) {}
