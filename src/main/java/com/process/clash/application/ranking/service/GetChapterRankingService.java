package com.process.clash.application.ranking.service;

import com.process.clash.application.ranking.data.GetChapterRankingData;
import com.process.clash.application.ranking.port.in.GetChapterRankingUseCase;
import com.process.clash.application.ranking.port.out.LoadChapterRankingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChapterRankingService implements GetChapterRankingUseCase {

    private final LoadChapterRankingPort loadChapterRankingPort;

    @Override
    public GetChapterRankingData.Result execute(GetChapterRankingData.Command command) {
        List<Object[]> rankingsWithMyRank = loadChapterRankingPort.loadRankingsWithMyRank(command.actor().id());

        GetChapterRankingData.MyRankingVo myRank = null;
        List<GetChapterRankingData.RankersVo> allRankers = new java.util.ArrayList<>();

        for (Object[] record : rankingsWithMyRank) {
            Long userId = ((Number) record[0]).longValue();
            String userName = (String) record[1];
            String profileImage = (String) record[2];
            Integer totalCompleted = ((Number) record[3]).intValue();
            Integer userRank = ((Number) record[4]).intValue();

            // 현재 사용자의 랭킹 정보 저장
            if (userId.equals(command.actor().id())) {
                myRank = new GetChapterRankingData.MyRankingVo(
                        userRank,
                        totalCompleted,
                        userId,
                        userName,
                        profileImage
                );
            }

            // 상위 20명만 allRankers에 추가
            if (userRank <= 20) {
                allRankers.add(new GetChapterRankingData.RankersVo(
                        userRank,
                        totalCompleted,
                        userId,
                        userName,
                        profileImage
                ));
            }
        }

        return new GetChapterRankingData.Result(myRank, allRankers);
    }
}
