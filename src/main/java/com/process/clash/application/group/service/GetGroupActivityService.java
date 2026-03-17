package com.process.clash.application.group.service;

import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.GetGroupActivityUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupMemberVo;
import com.process.clash.application.record.v2.util.RecordDayWindow;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.infrastructure.config.record.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GetGroupActivityService implements GetGroupActivityUseCase {

    private final GroupRepositoryPort groupRepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionRepositoryPort;
    private final GroupPolicy groupPolicy;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public GetGroupActivityData.Result execute(GetGroupActivityData.Command command) {
        if(!groupRepositoryPort.existsById(command.groupId())) {
            throw new GroupNotFoundException();
        }

        boolean isMember = groupRepositoryPort.existsMember(command.groupId(), command.actor().id());
        groupPolicy.validateMembership(isMember);

        List<User> members = groupRepositoryPort.findMembersByGroupId(command.groupId());
        List<Long> memberIds = members.stream()
            .map(User::id)
            .toList();

        RecordDayWindow todayWindow = RecordDayWindow.today(recordZoneId, recordProperties.dayBoundaryHour());
        LocalDate recordDate = command.date() == null ? todayWindow.recordDate() : command.date();
        RecordDayWindow dayWindow = recordDate.equals(todayWindow.recordDate())
            ? todayWindow
            : RecordDayWindow.of(recordDate, recordZoneId, recordProperties.dayBoundaryHour());

        Map<Long, Long> studyTimes = fetchStudyTimes(memberIds, dayWindow);
        Set<Long> activeUserIds = dayWindow.todayRecordDate()
            ? fetchActiveUserIds(memberIds)
            : Set.of();

        List<GroupMemberVo> memberVos = members.stream()
            .map(member -> GroupMemberVo.of(
                member.id(),
                member.name(),
                studyTimes.getOrDefault(member.id(), 0L),
                activeUserIds.contains(member.id())
            ))
            .toList();

        return new GetGroupActivityData.Result(memberVos);
    }

    private Map<Long, Long> fetchStudyTimes(List<Long> memberIds, RecordDayWindow dayWindow) {
        if (memberIds.isEmpty()) {
            return Map.of();
        }

        LocalDateTime startOfDay = dayWindow.dayStart();
        LocalDateTime endOfDay = dayWindow.dayEnd();

        return recordSessionRepositoryPort.getTotalStudyTimeInSecondsByUserIds(
            memberIds,
            startOfDay,
            endOfDay
        );
    }

    private Set<Long> fetchActiveUserIds(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return Set.of();
        }

        List<RecordSessionV2> activeSessions = recordSessionRepositoryPort.findAllActiveSessionsByUserIds(memberIds);
        return activeSessions.stream()
            .map(RecordSessionV2::userId)
            .collect(Collectors.toSet());
    }
}
