package com.process.clash.application.roadmap.section.port.in;

import com.process.clash.application.roadmap.section.data.GetSectionsData;

public interface GetSectionsUseCase {
    GetSectionsData.Result execute(GetSectionsData.Command command);
}
