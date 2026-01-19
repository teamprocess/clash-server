package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.AbleRivalInfo;
import com.process.clash.application.compete.rival.data.SearchRivalByKeywordData;
import com.process.clash.application.compete.rival.port.in.SearchRivalByKeywordUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchRivalByKeywordService implements SearchRivalByKeywordUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Override
    public SearchRivalByKeywordData.Result execute(SearchRivalByKeywordData.Command command) {

        List<Rival> rivals = rivalRepositoryPort.findAllByMyId(command.actor().id());

        List<Long> excludedUserIds = Stream.concat(
                rivals.stream().map(Rival::opponentId),
                Stream.of(command.actor().id())
        ).toList();

        List<AbleRivalInfo> ableRivalInfos = userGitHubRepositoryPort.findAbleRivalsWithUserInfoByKeyword(excludedUserIds, command.keyword());

        return SearchRivalByKeywordData.Result.from(ableRivalInfos);
    }
}
