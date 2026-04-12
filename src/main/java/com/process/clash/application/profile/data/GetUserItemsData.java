package com.process.clash.application.profile.data;

import com.process.clash.domain.common.enums.UserItemCategory;

public class GetUserItemsData {

    public record Command(
        Long targetUserId,
        UserItemCategory category
    ) {}
}
