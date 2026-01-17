package com.process.clash.adapter.web.roadmap.section.dto;

import com.process.clash.application.roadmap.section.data.GetSectionsData;

import java.util.List;

public class GetSectionsDto {

    public record Response(
            List<SectionVo> sections,
            List<String> categories
    ) {
        public static Response from(GetSectionsData.Result result) {
            List<SectionVo> sections = result.sections().stream()
                    .map(SectionVo::from)
                    .toList();
            return new Response(sections, result.categories());
        }
    }

    public record SectionVo(
            Long id,
            String title,
            String category,
            Boolean completed,
            Boolean locked
    ) {
        public static SectionVo from(GetSectionsData.Result.SectionVo vo) {
            return new SectionVo(vo.id(), vo.title(), vo.category(), vo.completed(), vo.locked());
        }
    }
}
