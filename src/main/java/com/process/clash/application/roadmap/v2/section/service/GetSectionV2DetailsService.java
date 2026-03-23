package com.process.clash.application.roadmap.v2.section.service;

import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2DetailsData;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2DetailsUseCase;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetSectionV2DetailsService implements GetSectionV2DetailsUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final ChapterV2RepositoryPort chapterV2RepositoryPort;
    private final UserSectionProgressRepositoryPort userSectionProgressRepository;

    @Override
    public GetSectionV2DetailsData.Result execute(GetSectionV2DetailsData.Command command) {
        Section section = sectionRepository.findById(command.sectionId())
                .orElseThrow(SectionNotFoundException::new);

        List<ChapterV2> chapters = chapterV2RepositoryPort.findAllBySectionId(command.sectionId());
        Map<Long, ChapterV2> chapterMap = chapters.stream()
                .collect(Collectors.toMap(ChapterV2::getId, Function.identity()));

        Optional<UserSectionProgress> progressOpt = userSectionProgressRepository
                .findByUserIdAndSectionId(command.actor().id(), command.sectionId());

        Boolean completed = progressOpt
                .map(UserSectionProgress::getIsCompleted)
                .orElse(false);

        Long currentChapterId = progressOpt.map(UserSectionProgress::getCurrentChapterId).orElse(null);
        Integer currentOrderIndex = progressOpt
                .map(UserSectionProgress::getCurrentChapterId)
                .map(chapterMap::get)
                .map(ChapterV2::getOrderIndex)
                .orElse(null);

        return GetSectionV2DetailsData.Result.from(section, completed, currentChapterId, currentOrderIndex, chapters);
    }
}
