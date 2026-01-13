package com.process.clash.application.major.port.in;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.major.MajorQuestion;

import java.util.List;

public interface GetMajorQuestionUseCase {
    List<MajorQuestion> findAllByMajor(Major major);
}
