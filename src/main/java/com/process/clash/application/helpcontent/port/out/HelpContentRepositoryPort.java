package com.process.clash.application.helpcontent.port.out;

import com.process.clash.domain.helpcontent.entity.HelpContent;

import java.util.Optional;

public interface HelpContentRepositoryPort {

    Optional<HelpContent> findByKey(String key);

    HelpContent save(HelpContent helpContent);
}
