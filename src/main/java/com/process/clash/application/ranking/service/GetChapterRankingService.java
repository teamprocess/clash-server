package com.process.clash.application.ranking.service;

import com.process.clash.adapter.persistence.roadmap.sectionprogress.UserSectionProgressJpaEntity;
import com.process.clash.adapter.persistence.roadmap.sectionprogress.UserSectionProgressJpaRepository;
import com.process.clash.application.ranking.data.GetChapterRankingData;
import com.process.clash.application.ranking.port.in.GetChapterRankingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChapterRankingService implements GetChapterRankingUseCase {

    private final UserSectionProgressJpaRepository userSectionProgressJpaRepository;

    @Override
    public GetChapterRankingData.Result execute(GetChapterRankingData.Command command) {
        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(command.actor().id());
        return null;
    }
}
