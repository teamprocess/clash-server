package com.process.clash.application.ranking.port.out;

import java.util.List;

public interface LoadChapterRankingPort {

    List<Object[]> loadRankingsWithMyRank(Long targetUserId);
}
