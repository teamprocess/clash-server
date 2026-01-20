package com.process.clash.application.roadmap.chapter.service;

import com.process.clash.application.roadmap.chapter.data.GetChapterDetailsData;
import com.process.clash.application.roadmap.chapter.port.in.GetChapterDetailsUseCase;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.ChapterNotFoundException;
import com.process.clash.application.roadmap.port.out.ChapterRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetChapterDetailsService implements GetChapterDetailsUseCase {

    private final ChapterRepositoryPort chapterRepositoryPort;

    @Override
    public GetChapterDetailsData.Result execute(GetChapterDetailsData.Command command) {
        Chapter chapter = chapterRepositoryPort.findById(command.chapterId())
                .orElseThrow(ChapterNotFoundException::new);

        return GetChapterDetailsData.Result.from(chapter);
    }
}
