package com.process.clash.adapter.web.section.dto;

import java.util.List;

public class GetSectionsDto {

    public record Response(
            List<SectionVo> sections,
            List<String> categories
    ) {
        // TODO: Implement from() method when Result is available
        // public static Response from(GetSectionsData.Result result) { ... }
    }

    public record SectionVo(
            Long id,
            String title,
            String category,
            Boolean completed,
            Boolean locked
    ) {
        // TODO: Implement from() method when Result.SectionVo is available
        // public static SectionVo from(GetSectionsData.Result.SectionVo vo) { ... }
    }
}
