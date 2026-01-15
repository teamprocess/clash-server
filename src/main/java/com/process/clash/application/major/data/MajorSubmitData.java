package com.process.clash.application.major.data;

import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;

public class MajorSubmitData {

    public record Command (
            Actor actor,
            Major major
    ) {
    }

}
