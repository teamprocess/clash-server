package com.process.clash.application.mainpage.port.in.rival;

import com.process.clash.application.compete.rival.data.SearchRivalByKeywordData;

public interface SearchRivalByKeywordUseCase {

    SearchRivalByKeywordData.Result execute(SearchRivalByKeywordData.Command command);
}
