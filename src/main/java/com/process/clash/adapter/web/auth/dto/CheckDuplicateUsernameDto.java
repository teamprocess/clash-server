package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.CheckDuplicateUsernameData;

public class CheckDuplicateUsernameDto {

    public record Request(
            String username
    ) {
        public CheckDuplicateUsernameData.Command toCommand() {
            return new CheckDuplicateUsernameData.Command(username);
        }
    }

    public record Response(
            boolean duplicated
    ) {}
}
