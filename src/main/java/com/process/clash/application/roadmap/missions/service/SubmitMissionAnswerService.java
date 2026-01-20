package com.process.clash.application.roadmap.missions.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.missions.data.SubmitMissionAnswerData;
import com.process.clash.application.roadmap.missions.exception.exception.badrequest.InvalidChoiceException;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.MissionNotFoundException;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.QuestionNotFoundException;
import com.process.clash.application.roadmap.missions.port.in.SubmitMissionAnswerUseCase;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import com.process.clash.domain.roadmap.entity.Choice;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.MissionQuestion;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmitMissionAnswerService implements SubmitMissionAnswerUseCase {

    private final MissionRepositoryPort missionRepositoryPort;
    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;

    @Override
    public SubmitMissionAnswerData.Result execute(SubmitMissionAnswerData.Command command) {
        Actor actor = command.actor();
        // 미션 조회 (N+1 방지: questions와 choices 함께 fetch)
        Mission mission = missionRepositoryPort.findByIdWithQuestions(command.missionId())
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

        // 사용자 미션 히스토리 조회 또는 생성
        Optional<UserMissionHistory> historyOpt = userMissionHistoryRepositoryPort.findByUserIdAndMissionId(actor.id(), command.missionId());
        UserMissionHistory history;
        if (historyOpt.isPresent()) {
            history = historyOpt.get();
        } else {
            history = UserMissionHistory.create(
                    actor.id(),
                    command.missionId(),
                    Optional.ofNullable(mission.getQuestions()).map(List::size).orElse(0)
            );
        }

        // 정답이면 correctCount 증가
        if (isCorrect) {
            history.recordCorrectAnswer();
        }

        // 답을 제출했으므로 currentQuestionIndex 증가
        history.recordQuestionAttempt();

        // 히스토리 저장
        userMissionHistoryRepositoryPort.save(history);

        // 진행 상황 계산
        int currentProgress = history.getCurrentQuestionIndex();
        int totalQuestion = history.getTotalCount();

        return new SubmitMissionAnswerData.Result(
                isCorrect,
                question.getExplanation(),
                currentProgress,
                totalQuestion,
                correctChoiceId
        );
    }
}