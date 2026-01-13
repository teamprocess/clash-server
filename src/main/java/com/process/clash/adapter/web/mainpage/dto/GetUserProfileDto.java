package com.process.clash.adapter.web.mainpage.dto;

import lombok.Builder;

public class GetUserProfileDto {
    @Builder
    public record Response(
            Long id,
            String name,
            String username,
            String profileImage
    ) {}
}
