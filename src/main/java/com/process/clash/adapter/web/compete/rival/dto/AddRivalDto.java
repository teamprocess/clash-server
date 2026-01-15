package com.process.clash.adapter.web.compete.rival.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AddRivalDto {

    public record Request(
            @NotEmpty(message = "아이디 목록은 비워둘 수 없습니다.")
            List<Id> ids
    ) { }

    public record Id(
            Long id
    ) {}
}
