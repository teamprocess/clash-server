package com.process.clash.adapter.persistence.roadmap.v2.chapter;

import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.choice.ChoiceV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaEntity;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.infrastructure.config.JpaAuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
class ChapterV2JpaRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ChapterV2JpaRepository chapterV2JpaRepository;

    private Long chapterId;

    @BeforeEach
    void setUp() {
        CategoryJpaEntity category = new CategoryJpaEntity(null, "백엔드", null, null, null);
        em.persist(category);

        SectionJpaEntity section = new SectionJpaEntity(
                null, Major.SERVER, "Spring", "설명",
                category, 1, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), null, null
        );
        em.persist(section);

        ChapterV2JpaEntity chapter = new ChapterV2JpaEntity(
                null, section, "챕터1", "챕터 설명", 1, null, new ArrayList<>(), null, null
        );
        em.persist(chapter);

        QuestionV2JpaEntity q1 = new QuestionV2JpaEntity(
                null, chapter, "질문1", "해설1", 1, 1, new ArrayList<>(), null, null
        );
        em.persist(q1);

        QuestionV2JpaEntity q2 = new QuestionV2JpaEntity(
                null, chapter, "질문2", "해설2", 2, 2, new ArrayList<>(), null, null
        );
        em.persist(q2);

        ChoiceV2JpaEntity c1 = new ChoiceV2JpaEntity(null, q1, "선택지A", false, 1, null, null);
        ChoiceV2JpaEntity c2 = new ChoiceV2JpaEntity(null, q1, "선택지B", true, 2, null, null);
        ChoiceV2JpaEntity c3 = new ChoiceV2JpaEntity(null, q2, "선택지C", true, 1, null, null);
        em.persist(c1);
        em.persist(c2);
        em.persist(c3);

        em.flush();
        em.clear();

        chapterId = chapter.getId();
    }

    @Test
    void findByIdWithQuestionsAndChoices_MultipleBagFetchException가_발생하지_않는다() {
        // questions(Bag) + choices(Bag) 동시 JOIN 시 MultipleBagFetchException 발생
        // @EntityGraph를 questions만으로 제한하고 choices는 batch fetch로 위임하여 해결
        assertThatNoException()
                .isThrownBy(() -> chapterV2JpaRepository.findByIdWithQuestionsAndChoices(chapterId));
    }

    @Test
    void findByIdWithQuestionsAndChoices_질문과_선택지_모두_조회된다() {
        Optional<ChapterV2JpaEntity> result = chapterV2JpaRepository.findByIdWithQuestionsAndChoices(chapterId);

        assertThat(result).isPresent();
        ChapterV2JpaEntity chapter = result.get();
        assertThat(chapter.getQuestions()).hasSize(2);
        assertThat(chapter.getQuestions().get(0).getChoices()).hasSize(2);
        assertThat(chapter.getQuestions().get(1).getChoices()).hasSize(1);
    }

    @Test
    void findByIdWithQuestionsAndChoices_존재하지_않는_id는_empty를_반환한다() {
        Optional<ChapterV2JpaEntity> result = chapterV2JpaRepository.findByIdWithQuestionsAndChoices(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdWithQuestionsAndChoices_질문이_없는_챕터는_빈_리스트를_반환한다() {
        CategoryJpaEntity category = new CategoryJpaEntity(null, "프론트엔드", null, null, null);
        em.persist(category);

        SectionJpaEntity section = new SectionJpaEntity(
                null, Major.SERVER, "React", "설명",
                category, 2, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), null, null
        );
        em.persist(section);

        ChapterV2JpaEntity emptyChapter = new ChapterV2JpaEntity(
                null, section, "빈챕터", "설명", 1, null, new ArrayList<>(), null, null
        );
        em.persist(emptyChapter);
        em.flush();
        em.clear();

        Optional<ChapterV2JpaEntity> result = chapterV2JpaRepository.findByIdWithQuestionsAndChoices(emptyChapter.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getQuestions()).isEmpty();
    }
}
