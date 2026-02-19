package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetCurrentRecordData;
import com.process.clash.application.record.port.in.GetCurrentRecordUseCase;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentRecordService implements GetCurrentRecordUseCase {

    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final ZoneId recordZoneId;

    @Override
    public GetCurrentRecordData.Result execute(GetCurrentRecordData.Command command) {
        return recordSessionRepositoryPort.findActiveSessionByUserId(command.actor().id())
            .map(session -> GetCurrentRecordData.Result.from(
                RecordSessionMapper.toSession(session, recordZoneId)
            ))
            .orElseGet(() -> GetCurrentRecordData.Result.from(null));
    }
}
