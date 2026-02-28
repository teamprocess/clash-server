package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.in.UpdateTaskCompletionV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTaskCompletionV2Service implements UpdateTaskCompletionV2UseCase {

    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Override
    public UpdateTaskCompletionV2Data.Result execute(UpdateTaskCompletionV2Data.Command command) {
        RecordTaskV2 task = recordTaskV2RepositoryPort.findByIdAndUserId(command.taskId(), command.actor().id())
            .orElseThrow(TaskV2NotFoundException::new);
        RecordTaskV2 savedTask = recordTaskV2RepositoryPort.save(task.changeCompleted(command.completed()));

        return UpdateTaskCompletionV2Data.Result.from(savedTask);
    }
}
