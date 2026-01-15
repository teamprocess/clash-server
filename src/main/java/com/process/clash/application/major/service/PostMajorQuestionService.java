package com.process.clash.application.major.service;

import com.process.clash.application.major.data.PostMajorQuestionData;
import com.process.clash.application.major.port.in.PostMajorQuestionUseCase;
import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.major.entity.MajorQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostMajorQuestionService implements PostMajorQuestionUseCase {

    private final MajorQuestionRepositoryPort majorQuestionRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public PostMajorQuestionData.Result execute(PostMajorQuestionData.Command command) {
        // Admin 권한 확인
        checkAdminPolicy.check(command.actor());

        // 도메인 엔티티 생성 및 저장
        MajorQuestion majorQuestion = command.toDomain();
        MajorQuestion savedMajorQuestion = majorQuestionRepositoryPort.save(majorQuestion);

        // 결과 반환
        return PostMajorQuestionData.Result.from(savedMajorQuestion);
    }
}
