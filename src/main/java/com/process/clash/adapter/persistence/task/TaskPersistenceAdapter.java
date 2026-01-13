package com.process.clash.adapter.persistence.task;

import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.domain.user.model.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements UserRepositoryPort {

    private final TaskJpaRepository taskJpaRepository;
    private final TaskJpaMapper taskJpaMapper;

    @Override
    public void save(User user) {
        TaskJpaEntity taskJpaEntity = taskJpaMapper.toJpaEntity(user);
        userJpaRepository.save(taskJpaEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(taskJpaMapper::toDomain);
    }
}
