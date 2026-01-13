package com.process.clash.application.record.port.out;

import com.process.clash.domain.record.model.entity.Session;
import java.util.List;
import java.util.Optional;

public interface SessionRepositoryPort {
    void save(Session session);
    Optional<Session> findById(Long id);
    List<Session> findAllByUserId(Long userId);
}
