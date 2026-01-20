package com.process.clash.application.missions.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.process.clash.application.missions.exception.exception.badrequest.InvalidChoiceException;
import com.process.clash.application.missions.exception.exception.notfound.MissionNotFoundException;
import com.process.clash.application.missions.exception.exception.notfound.QuestionNotFoundException;
import com.process.clash.application.missions.port.in.SubmitMissionAnswerUseCase;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Choice;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.MissionQuestion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmitMissionAnswerService implements SubmitMissionAnswerUseCase {

    private final MissionRepositoryPort missionRepositoryPort;

    @Override
    public Result execute(Command command) {
        // 미션 조회 (N+1 방지: questions와 choices 함께 fetch)
        Mission mission = missionRepositoryPort.findByIdWithQuestionsAndChoices(command.missionId())
                .orElseThrow(MissionNotFoundException::new);

        // 질문 조회
        MissionQuestion question = Optional.ofNullable(mission.getQuestions())
                .orElse(List.of())
                .stream()
                .filter(q -> q.getId().equals(command.questionId()))
                .findFirst()
                .orElseThrow(QuestionNotFoundException::new);

        // 선택지 검증
        List<Choice> choices = Optional.ofNullable(question.getChoices()).orElse(List.of());

        Choice submittedChoice = choices.stream()
                .filter(c -> c.getId().equals(command.submittedChoiceId()))
                .findFirst()
                .orElseThrow(InvalidChoiceException::new);

        boolean isCorrect = submittedChoice.isCorrect();

        Long correctChoiceId = choices.stream()
                .filter(Choice::isCorrect)
                .findFirst()
                .map(Choice::getId)
                .orElse(null);

        // 진행 상황 계산 (임시)
        int currentProgress = 1;
        int totalQuestion = Optional.ofNullable(mission.getQuestions()).map(List::size).orElse(0);

        return new Result(
                isCorrect,
                question.getExplanation(),
                currentProgress,
                totalQuestion,
                correctChoiceId
        );
    }
}