package com.process.clash.application.helpcontent.data;

import com.process.clash.domain.helpcontent.entity.HelpContent;

public class GetHelpContentData {

    public record Result(String key, String content, long version) {
        public static Result from(HelpContent helpContent) {
            return new Result(helpContent.key(), helpContent.content(), helpContent.version());
        }
    }
}
