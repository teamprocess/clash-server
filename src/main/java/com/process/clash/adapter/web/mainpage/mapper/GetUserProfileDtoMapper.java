package com.process.clash.adapter.web.mainpage.mapper;

import com.process.clash.adapter.web.mainpage.dto.GetUserProfileDto;
import com.process.clash.application.mainpage.data.GetUserProfileData;
import org.springframework.stereotype.Component;

@Component
public class GetUserProfileDtoMapper {
    public static GetUserProfileDto.Response toResponse(GetUserProfileData.Result result) {
        return GetUserProfileDto.Response.builder()
                .id(result.id())
                .name(result.name())
                .username(result.username())
                .profileImage(result.profileImage())
                .build();
    }
}
