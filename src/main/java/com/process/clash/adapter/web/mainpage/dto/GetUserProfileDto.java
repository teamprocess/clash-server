package com.process.clash.adapter.web.mainpage.dto;

import com.process.clash.application.mainpage.data.GetUserProfileData;

public class GetUserProfileDto {
    public record Response(
            Long id,
            String name,
            String username,
            String profileImage
    ) {}

    public static GetUserProfileDto.Response from(GetUserProfileData.Result result) {
        return new GetUserProfileDto.Response(
                result.id(),
                result.name(),
                result.username(),
                result.profileImage()
        );
    }
}
