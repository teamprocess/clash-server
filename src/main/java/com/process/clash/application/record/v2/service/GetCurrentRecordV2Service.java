package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.GetCurrentRecordV2Data;
import com.process.clash.application.record.v2.port.in.GetCurrentRecordV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentRecordV2Service implements GetCurrentRecordV2UseCase {

    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final ZoneId recordZoneId;

    @Override
    public GetCurrentRecordV2Data.Result execute(GetCurrentRecordV2Data.Command command) {
        return recordSessionV2RepositoryPort.findActiveSessionByUserId(command.actor().id())
            .map(session -> GetCurrentRecordV2Data.Result.from(
                RecordSessionV2Mapper.toSession(session, recordZoneId)
            ))
            .orElseGet(() -> GetCurrentRecordV2Data.Result.from(null));
    }
}
