package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.GetSectionsData;
import com.process.clash.application.roadmap.section.port.in.GetSectionsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSectionsService implements GetSectionsUseCase {

    @Override
    public GetSectionsData.Result execute(GetSectionsData.Command command) {
        return null;
    }
}
