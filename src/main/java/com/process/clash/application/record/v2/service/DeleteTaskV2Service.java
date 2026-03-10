package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.DeleteTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.conflict.TaskV2HasActiveSessionException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.in.DeleteTaskV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskV2Service implements DeleteTaskV2UseCase {

    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Override
    public void execute(DeleteTaskV2Data.Command command) {
        RecordTaskV2 task = recordTaskV2RepositoryPort.findByIdAndUserId(command.taskId(), command.actor().id())
            .orElseThrow(TaskV2NotFoundException::new);

        if (recordSessionV2RepositoryPort.existsActiveSessionByTaskId(task.id())) {
            throw new TaskV2HasActiveSessionException();
        }

        try {
            recordTaskV2RepositoryPort.deleteById(task.id());
        } catch (DataIntegrityViolationException exception) {
            throw new TaskV2HasActiveSessionException(exception);
        }
    }
}
