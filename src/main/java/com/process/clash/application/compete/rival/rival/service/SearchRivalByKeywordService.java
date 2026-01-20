package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfo;
import com.process.clash.application.compete.rival.rival.data.SearchRivalByKeywordData;
import com.process.clash.application.compete.rival.rival.port.in.SearchRivalByKeywordUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.application.user.usergithub.port.out.UserGitHubRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchRivalByKeywordService implements SearchRivalByKeywordUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final UserGitHubRepositoryPort userGitHubRepositoryPort;

    @Override
    public SearchRivalByKeywordData.Result execute(SearchRivalByKeywordData.Command command) {

        Long myId = command.actor().id();

        List<Rival> rivals = rivalRepositoryPort.findAllByUserId(myId);

        List<Long> excludedUserIds = Stream.concat(
                rivals.stream()
                        .map(rival -> {
                            if (rival.firstUserId().equals(myId)) {
                                return rival.secondUserId();
                            } else {
                                return rival.firstUserId();
                            }
                        }),
                Stream.of(myId)
        ).toList();

        List<AbleRivalInfo> ableRivalInfos =
                userGitHubRepositoryPort.findAbleRivalsWithUserInfoByKeyword(
                        excludedUserIds,
                        command.keyword()
                );

        return SearchRivalByKeywordData.Result.from(ableRivalInfos);
    }

}
