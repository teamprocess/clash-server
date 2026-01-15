package com.process.clash.application.mainpage.service.rival;

import com.process.clash.application.compete.rival.data.AddRivalData;
import com.process.clash.application.compete.rival.port.in.AddRivalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddRivalService implements AddRivalUseCase {

    @Override
    public void execute(AddRivalData.Command command) {

    }
}
