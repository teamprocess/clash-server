package com.process.clash.application.roadmap.v2.section.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2DetailsData;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Category;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetSectionV2DetailsServiceTest {

    @Mock
    private SectionRepositoryPort sectionRepository;

    @Mock
    private ChapterV2RepositoryPort chapterV2RepositoryPort;

    @Mock
    private UserSectionProgressRepositoryPort userSectionProgressRepository;

    private GetSectionV2DetailsService service;

    @BeforeEach
    void setUp() {
        service = new GetSectionV2DetailsService(
                sectionRepository,
                chapterV2RepositoryPort,
                userSectionProgressRepository
        );
    }

    @Test
    @DisplayName("섹션 진행도가 있으면 completed 필드에 완료 여부를 담아 반환한다")
    void execute_returnsCompletedFromProgress() {
        Actor actor = new Actor(1L);
        Section section = createSection(10L, "Spring");
        ChapterV2 chapter = new ChapterV2(100L, 10L, "Intro", "desc", 0, "https://example.com", List.of());
        UserSectionProgress progress = new UserSectionProgress(1L, 1L, 10L, 100L, 3, true);

        when(sectionRepository.findById(10L)).thenReturn(Optional.of(section));
        when(chapterV2RepositoryPort.findAllBySectionId(10L)).thenReturn(List.of(chapter));
        when(userSectionProgressRepository.findByUserIdAndSectionId(1L, 10L)).thenReturn(Optional.of(progress));

        GetSectionV2DetailsData.Result result = service.execute(new GetSectionV2DetailsData.Command(actor, 10L));

        assertThat(result.completed()).isTrue();
        assertThat(result.currentChapterId()).isEqualTo(100L);
        assertThat(result.currentOrderIndex()).isEqualTo(0);
    }

    @Test
    @DisplayName("섹션 진행도가 없으면 completed 필드는 false다")
    void execute_returnsFalseWhenProgressMissing() {
        Actor actor = new Actor(1L);
        Section section = createSection(10L, "Spring");
        ChapterV2 chapter = new ChapterV2(100L, 10L, "Intro", "desc", 0, "https://example.com", List.of());

        when(sectionRepository.findById(10L)).thenReturn(Optional.of(section));
        when(chapterV2RepositoryPort.findAllBySectionId(10L)).thenReturn(List.of(chapter));
        when(userSectionProgressRepository.findByUserIdAndSectionId(1L, 10L)).thenReturn(Optional.empty());

        GetSectionV2DetailsData.Result result = service.execute(new GetSectionV2DetailsData.Command(actor, 10L));

        assertThat(result.completed()).isFalse();
        assertThat(result.currentChapterId()).isNull();
        assertThat(result.currentOrderIndex()).isNull();
    }

    private Section createSection(Long sectionId, String title) {
        Category category = new Category(1L, "Backend", "https://example.com/image.png", Instant.now(), Instant.now());
        return new Section(
                sectionId,
                Major.SERVER,
                title,
                "desc",
                category,
                0,
                List.of(),
                List.of(),
                Set.of(),
                Instant.now(),
                Instant.now()
        );
    }
}
