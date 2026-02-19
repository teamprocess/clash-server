package com.process.clash.application.group.service;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.exception.exception.notfound.GroupNotFoundException;
import com.process.clash.application.group.policy.GroupPolicy;
import com.process.clash.application.group.port.in.GetGroupActivityUseCase;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.application.group.vo.GroupMemberVo;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.infrastructure.config.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    private static final int PAGE_SIZE = 10;

    private final GroupRepositoryPort groupRepositoryPort;
    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
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

        GroupRepositoryPort.MemberPageResult memberPage =
            groupRepositoryPort.findMembersByGroupId(command.groupId(), command.page(), PAGE_SIZE);
        List<User> members = memberPage.members();
        List<Long> memberIds = members.stream()
            .map(User::id)
            .toList();

        Map<Long, Long> studyTimes = fetchTodayStudyTimes(memberIds);
        Set<Long> activeUserIds = fetchActiveUserIds();

        List<GroupMemberVo> memberVos = members.stream()
            .map(member -> GroupMemberVo.of(
                member.id(),
                member.name(),
                studyTimes.getOrDefault(member.id(), 0L),
                activeUserIds.contains(member.id())
            ))
            .toList();

        Pagination pagination =
            Pagination.from(command.page(), PAGE_SIZE, memberPage.totalCount());

        return new GetGroupActivityData.Result(memberVos, pagination);
    }

    private Map<Long, Long> fetchTodayStudyTimes(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return Map.of();
        }

        // Day starts at the configured boundary hour (e.g., 6 AM) in the record timezone.
        int boundaryHour = recordProperties.dayBoundaryHour();
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        LocalDate recordDate = nowZoned.toLocalDate();
        if (nowZoned.getHour() < boundaryHour) {
            recordDate = recordDate.minusDays(1);
        }
        LocalDateTime startOfDay = recordDate.atTime(boundaryHour, 0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return recordSessionRepositoryPort.getTotalStudyTimeInSecondsByUserIds(memberIds, startOfDay, endOfDay);
    }

    private Set<Long> fetchActiveUserIds() {
        List<RecordSession> activeSessions = recordSessionRepositoryPort.findAllActiveSessions();
        return activeSessions.stream()
            .map(session -> session.user().id())
            .collect(Collectors.toSet());
    }
}
