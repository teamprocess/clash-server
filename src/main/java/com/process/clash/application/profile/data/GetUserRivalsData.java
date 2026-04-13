package com.process.clash.application.profile.data;

public class GetUserRivalsData {

    public record Command(
        Long targetUserId
    ) {}
}
