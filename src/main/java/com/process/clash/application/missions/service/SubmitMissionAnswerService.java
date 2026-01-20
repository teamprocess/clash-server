package com.process.clash.application.missions.service;

import java.util.List;

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

        // 질문 조회 (임시: 첫 번째 질문으로 가정)
        List<MissionQuestion> questions = mission.getQuestions();
        if (questions == null || questions.isEmpty()) {
            throw new QuestionNotFoundException();
        }

        MissionQuestion question = questions.get(0); // command.questionId() 사용해야 하지만 임시

        // 선택지 검증
        List<Choice> choices = question.getChoices();
        if (choices == null) {
            throw new InvalidChoiceException();
        }

        boolean isCorrect = false;
        Long correctChoiceId = null;
        boolean choiceFound = false;
        for (Choice choice : choices) {
            if (choice.isCorrect()) {
                correctChoiceId = choice.getId();
            }
            if (choice.getId().equals(command.submittedChoiceId())) {
                isCorrect = choice.isCorrect();
                choiceFound = true;
            }
        }

        if (!choiceFound) {
            throw new InvalidChoiceException();
        }

        // 진행 상황 계산 (임시)
        int currentProgress = 1;
        int totalQuestion = questions.size();

        return new Result(
                isCorrect,
                question.getExplanation(),
                currentProgress,
                totalQuestion,
                correctChoiceId
        );
    }
}