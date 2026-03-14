package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.compete.rival.rival.data.FindAllRivalsData;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;

import java.util.List;

public class FindAllRivalsDto {

    public record Response(
            List<RivalInfo> rivals
    ) {
        public static Response from(FindAllRivalsData.Result result) {
            return new Response(
                    result.rivals().stream()
                            .map(info -> new RivalInfo(
                                    info.rivalId(),
                                    info.githubId(),
                                    info.name(),
                                    info.profileImage(),
                                    info.rivalLinkingStatus(),
                                    info.rankTier(),
                                    info.expTier()
                            ))
                            .toList()
            );
        }
    }

    private record RivalInfo(
            Long rivalId,
            String githubId,
            String name,
            String profileImage,
            RivalLinkingStatus rivalLinkingStatus,
            RankTier rankTier,
            ExpTier expTier
    ) {}
}
