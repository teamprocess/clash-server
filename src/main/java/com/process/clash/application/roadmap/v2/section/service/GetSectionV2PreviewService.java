package com.process.clash.application.roadmap.v2.section.service;

import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2PreviewData;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2PreviewUseCase;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetSectionV2PreviewService implements GetSectionV2PreviewUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final ChapterV2RepositoryPort chapterV2RepositoryPort;

    @Override
    public GetSectionV2PreviewData.Result execute(GetSectionV2PreviewData.Command command) {
        Section section = sectionRepository.findById(command.sectionId())
                .orElseThrow(SectionNotFoundException::new);

        List<ChapterV2> chapters = chapterV2RepositoryPort.findAllBySectionId(command.sectionId());

        return GetSectionV2PreviewData.Result.from(section, chapters);
    }
}