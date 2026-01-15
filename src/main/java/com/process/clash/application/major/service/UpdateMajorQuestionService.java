package com.process.clash.application.major.service;

import com.process.clash.application.major.data.UpdateMajorQuestionData;
import com.process.clash.application.major.exception.exception.notfound.MajorQuestionNotFoundException;
import com.process.clash.application.major.port.in.UpdateMajorQuestionUseCase;
import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.major.entity.MajorQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateMajorQuestionService implements UpdateMajorQuestionUseCase {

    private final MajorQuestionRepositoryPort majorQuestionRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public UpdateMajorQuestionData.Result execute(UpdateMajorQuestionData.Command command) {
        checkAdminPolicy.check(command.actor());

        MajorQuestion existingQuestion = majorQuestionRepositoryPort.findById(command.questionId())
                .orElseThrow(MajorQuestionNotFoundException::new);

        // 부분 업데이트: null이 아닌 필드만 업데이트
        String updatedContent = command.content() != null ? command.content() : existingQuestion.getContent();
        MajorQuestion.WeightVo updatedWeight = mergeWeight(existingQuestion.getWeightVo(), command.weight());

        MajorQuestion updatedQuestion = new MajorQuestion(
                existingQuestion.getId(),
                updatedContent,
                updatedWeight,
                existingQuestion.getCreatedAt(),
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );

        MajorQuestion savedQuestion = majorQuestionRepositoryPort.save(updatedQuestion);
        return UpdateMajorQuestionData.Result.from(savedQuestion);
    }

    private MajorQuestion.WeightVo mergeWeight(MajorQuestion.WeightVo existing, UpdateMajorQuestionData.Command.WeightVo update) {
        if (update == null) {
            return existing;
        }

        return new MajorQuestion.WeightVo(
                update.web() != null ? update.web() : existing.getWeb(),
                update.app() != null ? update.app() : existing.getApp(),
                update.server() != null ? update.server() : existing.getServer(),
                update.ai() != null ? update.ai() : existing.getAi(),
                update.game() != null ? update.game() : existing.getGame()
        );
    }
}
