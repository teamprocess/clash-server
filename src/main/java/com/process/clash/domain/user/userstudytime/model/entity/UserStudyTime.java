package com.process.clash.domain.user.userstudytime.model.entity;

import java.util.Date;

public record UserStudyTime(
        Long id,
        Date date,
        Long userId
) {}
