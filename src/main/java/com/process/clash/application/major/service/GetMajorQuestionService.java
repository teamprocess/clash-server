package com.process.clash.application.major.service;

import com.process.clash.application.major.data.GetMajorQuestionResult;
import com.process.clash.application.major.port.in.GetMajorQuestionUseCase;
import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.domain.major.MajorQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMajorQuestionService implements GetMajorQuestionUseCase {

    private final MajorQuestionRepositoryPort majorQuestionRepositoryPort;

    @Override
    public GetMajorQuestionResult findAll() {
        List<MajorQuestion> majorQuestions = majorQuestionRepositoryPort.findAll();
        return GetMajorQuestionResult.from(majorQuestions);
    }
}
