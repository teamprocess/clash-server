package com.process.clash.adapter.web.auth.dto;

import com.process.clash.application.user.user.data.CheckDuplicateUsernameData;

public class CheckDuplicateUsernameDto {

    /**
     * Controller에서 @RequestParam 검증을 수행하므로 별도의 validation 불필요
     */
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
