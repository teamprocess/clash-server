package com.process.clash.application.mainpage.service.rival;

import com.process.clash.application.compete.rival.data.SearchRivalByKeywordData;
import com.process.clash.application.mainpage.port.in.rival.SearchRivalByKeywordUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchRivalByKeywordService implements SearchRivalByKeywordUseCase {

    @Override
    public SearchRivalByKeywordData.Result execute(SearchRivalByKeywordData.Command command) {

        return null;
    }
}
