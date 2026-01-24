package com.process.clash.domain.group.entity;

public record GroupMember (
    Long id,
    String name,
    Long studyTime,
    Boolean isStudying,
    Long groupId
) {}
