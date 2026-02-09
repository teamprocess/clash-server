package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "GitHub 활동 데이터")
public class GetMyGitHubActivityDataDocument {

    @Schema(description = "총 기여 수", example = "42")
    public Integer totalContributions;

    @ArraySchema(schema = @Schema(implementation = GetMyGitHubContributionDocument.class))
    public List<GetMyGitHubContributionDocument> contributions;
}
