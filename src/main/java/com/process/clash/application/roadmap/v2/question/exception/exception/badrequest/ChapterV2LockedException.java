package com.process.clash.application.roadmap.v2.question.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.v2.question.exception.status.QuestionV2StatusCode;

public class ChapterV2LockedException extends BadRequestException {

    public ChapterV2LockedException() {
        super(QuestionV2StatusCode.CHAPTER_V2_LOCKED);
    }
}
