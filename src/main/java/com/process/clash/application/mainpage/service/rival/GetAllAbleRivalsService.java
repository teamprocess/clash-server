package com.process.clash.application.mainpage.service.rival;

import com.process.clash.application.mainpage.data.rival.GetAllAbleRivalsData;
import com.process.clash.application.mainpage.port.in.rival.GetAllAbleRivalsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        return null;
    }
}
