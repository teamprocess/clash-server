package com.process.clash.application.group.vo;

public record GroupMemberVo(
    Long id,
    String name,
    Long studyTime,
    Boolean isStudying
) {
    public static GroupMemberVo of(Long id, String name, Long studyTime, Boolean isStudying) {
        return new GroupMemberVo(id, name, studyTime, isStudying);
    }
}
