package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;
import com.process.clash.application.roadmap.section.port.in.GetSectionDetailsUseCase;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSectionDetailsService implements GetSectionDetailsUseCase {

    private final SectionRepositoryPort sectionRepositoryPort;
    private final UserSectionProgressRepositoryPort userSectionProgressRepository;

    @Override
    public GetSectionDetailsData.Result execute(GetSectionDetailsData.Command command) {
        Section section = sectionRepositoryPort.findById(command.sectionId())
                .orElseThrow(SectionNotFoundException::new);

        UserSectionProgress userSectionProgress = userSectionProgressRepository
                .findByUserIdAndSectionId(command.actor().id(), command.sectionId())
                .orElse(new UserSectionProgress(null, command.actor().id(), command.sectionId(), null, 0, false));

        Long currentChapterId = userSectionProgress.getCurrentChapterId();

        return GetSectionDetailsData.Result.from(section, currentChapterId);
    }
}
