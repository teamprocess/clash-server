package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.GetSectionPreviewData;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.in.GetSectionPreviewUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetSectionPreviewService implements GetSectionPreviewUseCase {

    private final SectionRepositoryPort sectionRepository;

    @Override
    public GetSectionPreviewData.Result execute(GetSectionPreviewData.Command command) {

        Section section = sectionRepository.findById(command.sectionId()).orElseThrow(SectionNotFoundException::new);

        long size = section.getChapters().size();

        return GetSectionPreviewData.Result.from(section, size);
    }
}
