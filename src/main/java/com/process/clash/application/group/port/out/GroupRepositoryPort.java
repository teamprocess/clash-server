package com.process.clash.application.group.port.out;

import com.process.clash.domain.group.entity.Group;
import com.process.clash.domain.group.enums.GroupCategory;
import com.process.clash.domain.user.user.entity.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GroupRepositoryPort {

    Group save(Group group);

    Optional<Group> findById(Long groupId);

    void deleteById(Long groupId);

    PageResult findAllByPage(Integer page, Integer size);

    PageResult findAllByPageAndCategory(Integer page, Integer size, GroupCategory category);

    PageResult findAllByMemberUserId(Long userId, Integer page, Integer size);

    PageResult findAllByMemberUserIdAndCategory(Long userId, Integer page, Integer size, GroupCategory category);

    List<Long> findGroupIdsByMemberUserIdAndGroupIds(Long userId, List<Long> groupIds);

    boolean existsMember(Long groupId, Long userId);

    boolean existsById(Long groupId);

    void addMember(Long groupId, Long userId);

    void removeMember(Long groupId, Long userId);

    int countMembers(Long groupId);

    Map<Long, Integer> countMembersByGroupIds(List<Long> groupIds);

    MemberPageResult findMembersByGroupId(Long groupId, Integer page, Integer size);

    record PageResult(List<Group> groups, long totalCount) {}

    record MemberPageResult(List<User> members, long totalCount) {}
}
