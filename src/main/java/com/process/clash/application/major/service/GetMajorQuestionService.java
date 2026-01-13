package com.process.clash.application.major.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.process.clash.application.major.data.GetMajorQuestionData;
import com.process.clash.application.major.port.in.GetMajorQuestionUseCase;
import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.domain.major.entity.MajorQuestion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetMajorQuestionService implements GetMajorQuestionUseCase {

    private final MajorQuestionRepositoryPort majorQuestionRepositoryPort;

    @Override
    public GetMajorQuestionData.Result execute(GetMajorQuestionData.Command command) {
        List<MajorQuestion> majorQuestions = majorQuestionRepositoryPort.findAll();
        return GetMajorQuestionData.Result.from(majorQuestions);
    }
}
