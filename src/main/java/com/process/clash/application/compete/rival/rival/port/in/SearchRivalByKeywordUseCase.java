package com.process.clash.application.compete.rival.rival.port.in;

import com.process.clash.application.compete.rival.rival.data.SearchRivalByKeywordData;

public interface SearchRivalByKeywordUseCase {

    SearchRivalByKeywordData.Result execute(SearchRivalByKeywordData.Command command);
}
