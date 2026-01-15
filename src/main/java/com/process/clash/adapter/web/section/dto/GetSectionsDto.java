package com.process.clash.adapter.web.section.dto;

import java.util.List;

public class GetSectionsDto {

    public record Response(
            List<SectionVo> sections,
            List<String> categories
    ) {
        // TODO: Result 사용 가능 시 from() 메서드 구현 필요
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
