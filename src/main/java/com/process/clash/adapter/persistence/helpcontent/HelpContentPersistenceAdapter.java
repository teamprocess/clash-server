package com.process.clash.adapter.persistence.helpcontent;

import com.process.clash.application.helpcontent.port.out.HelpContentRepositoryPort;
import com.process.clash.domain.helpcontent.entity.HelpContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HelpContentPersistenceAdapter implements HelpContentRepositoryPort {

    private final HelpContentJpaRepository helpContentJpaRepository;
    private final HelpContentJpaMapper helpContentJpaMapper;

    @Override
    public Optional<HelpContent> findByKey(String key) {
        return helpContentJpaRepository.findById(key).map(helpContentJpaMapper::toDomain);
    }

    @Override
    public boolean existsByKey(String key) {
        return helpContentJpaRepository.existsById(key);
    }

    @Override
    public HelpContent save(HelpContent helpContent) {
        HelpContentJpaEntity entity = helpContentJpaMapper.toJpaEntity(helpContent);
        return helpContentJpaMapper.toDomain(helpContentJpaRepository.save(entity));
    }
}
