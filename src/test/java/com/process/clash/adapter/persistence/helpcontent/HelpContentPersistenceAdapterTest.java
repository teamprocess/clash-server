package com.process.clash.adapter.persistence.helpcontent;

import com.process.clash.domain.helpcontent.entity.HelpContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HelpContentPersistenceAdapterTest {

    @Mock
    private HelpContentJpaRepository helpContentJpaRepository;

    @Test
    @DisplayName("저장 후 flush된 version과 updatedAt을 반환한다")
    void save_returnsFlushedEntity() {
        HelpContentPersistenceAdapter adapter = new HelpContentPersistenceAdapter(
                helpContentJpaRepository,
                new HelpContentJpaMapper()
        );
        Instant createdAt = Instant.parse("2026-07-15T00:00:00Z");
        Instant updatedAt = Instant.parse("2026-07-15T01:00:00Z");
        HelpContentJpaEntity flushedEntity = new HelpContentJpaEntity(
                "cookie-tooltip",
                "변경 문구",
                4L,
                createdAt,
                updatedAt
        );
        when(helpContentJpaRepository.saveAndFlush(any())).thenReturn(flushedEntity);

        HelpContent saved = adapter.save(new HelpContent(
                "cookie-tooltip",
                "변경 문구",
                3L,
                createdAt,
                null
        ));

        verify(helpContentJpaRepository).saveAndFlush(any());
        assertThat(saved.version()).isEqualTo(4L);
        assertThat(saved.updatedAt()).isEqualTo(updatedAt);
    }
}
