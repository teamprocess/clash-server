package com.process.clash.adapter.persistence.group;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.group.port.out.GroupRepositoryPort;
import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.user.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupPersistenceAdapter implements GroupRepositoryPort {

    private final GroupJpaMapper groupJpaMapper;
    private final GroupJpaRepository groupJpaRepository;
    private final GroupMemberJpaRepository groupMemberJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    public Group save(Group group) {
        UserJpaEntity owner = userJpaRepository.getReferenceById(group.ownerId());
        GroupJpaEntity entity = groupJpaMapper.toJpaEntity(group, owner);
        GroupJpaEntity saved = groupJpaRepository.save(entity);
        return groupJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Group> findById(Long groupId) {
        return groupJpaRepository.findById(groupId)
            .map(groupJpaMapper::toDomain);
    }

    @Override
    public void deleteById(Long groupId) {
        groupMemberJpaRepository.deleteByGroupId(groupId);
        groupJpaRepository.deleteById(groupId);
    }

    @Override
    public PageResult findAllByPage(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GroupJpaEntity> pageResult = groupJpaRepository.findAll(pageable);
        return mapPageResult(pageResult);
    }

    @Override
    public PageResult findAllByMemberUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GroupJpaEntity> pageResult = groupJpaRepository.findAllByMemberUserId(userId, pageable);
        return mapPageResult(pageResult);
    }

    @Override
    public List<Long> findGroupIdsByMemberUserIdAndGroupIds(Long userId, List<Long> groupIds) {
        if (groupIds.isEmpty()) {
            return List.of();
        }
        return groupMemberJpaRepository.findGroupIdsByUserIdAndGroupIds(userId, groupIds);
    }

    @Override
    public boolean existsMember(Long groupId, Long userId) {
        return groupMemberJpaRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public void addMember(Long groupId, Long userId) {
        GroupJpaEntity group = groupJpaRepository.getReferenceById(groupId);
        UserJpaEntity user = userJpaRepository.getReferenceById(userId);
        GroupMemberJpaEntity member = new GroupMemberJpaEntity(null, group, user);
        groupMemberJpaRepository.save(member);
    }

    @Override
    public void removeMember(Long groupId, Long userId) {
        groupMemberJpaRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public long countMembers(Long groupId) {
        return groupMemberJpaRepository.countByGroupId(groupId);
    }

    @Override
    public MemberPageResult findMembersByGroupId(Long groupId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<GroupMemberJpaEntity> pageResult = groupMemberJpaRepository.findAllByGroupId(groupId, pageable);
        List<User> members = pageResult.getContent().stream()
            .map(member -> userJpaMapper.toDomain(member.getUser()))
            .toList();
        return new MemberPageResult(members, pageResult.getTotalElements());
    }

    @Override
    public Map<Long, Integer> countMembersByGroupIds(List<Long> groupIds) {
        return fetchMemberCounts(groupIds);
    }

    private PageResult mapPageResult(Page<GroupJpaEntity> pageResult) {
        List<GroupJpaEntity> entities = pageResult.getContent();
        List<Group> groups = entities.stream()
            .map(groupJpaMapper::toDomain)
            .toList();

        return new PageResult(groups, pageResult.getTotalElements());
    }

    private Map<Long, Integer> fetchMemberCounts(List<Long> groupIds) {
        if (groupIds.isEmpty()) {
            return Map.of();
        }

        return groupMemberJpaRepository.countAllByGroupIds(groupIds).stream()
            .collect(Collectors.toMap(
                GroupMemberJpaRepository.GroupMemberCountProjection::getGroupId,
                projection -> projection.getMemberCount().intValue()
            ));
    }
}
