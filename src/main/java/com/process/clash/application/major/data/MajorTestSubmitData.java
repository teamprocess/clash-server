package com.process.clash.application.major.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;

public class MajorTestSubmitData {

    public record Command (
            Actor actor,
            Major major
    ) {}

}
