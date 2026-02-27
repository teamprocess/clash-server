package com.process.clash.adapter.persistence.roadmap.sectionprogress;

import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaEntity;
import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaMapper;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.choice.ChoiceJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.adapter.persistence.roadmap.mission.MissionJpaMapper;
import com.process.clash.adapter.persistence.roadmap.missionquestion.MissionQuestionJpaMapper;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.JpaAuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        JpaAuditingConfig.class,
        UserJpaMapper.class,
        UserSectionProgressJpaMapper.class,
        CategoryJpaMapper.class,
        SectionKeyPointJpaMapper.class,
        ChoiceJpaMapper.class,
        MissionQuestionJpaMapper.class,
        MissionJpaMapper.class,
        ChapterJpaMapper.class,
        SectionJpaMapper.class
})
class UserSectionProgressJpaRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserSectionProgressJpaRepository userSectionProgressJpaRepository;

    @Autowired
    private UserJpaMapper userJpaMapper;

    @Autowired
    private UserSectionProgressJpaMapper userSectionProgressJpaMapper;

    UserJpaEntity noSectionProgressUserJpaEntity;
    UserJpaEntity sectionProgressUserJpaEntity;

    @BeforeEach
    void beforeEach() {
        Instant now = Instant.now();

        // 1. Category 저장
        CategoryJpaEntity categoryJpaEntity = new CategoryJpaEntity(null, "테스트 카테고리", null, now, now);
        em.persist(categoryJpaEntity);

        // 2. Section 저장 (Category 참조)
        SectionJpaEntity sectionJpaEntity = new SectionJpaEntity(
                null, Major.SERVER, "테스트 섹션", "테스트 설명",
                categoryJpaEntity, 1, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), now, now
        );
        em.persist(sectionJpaEntity);

        // 3. Chapter 저장 (Section 참조)
        ChapterJpaEntity chapterJpaEntity = new ChapterJpaEntity(
                null, sectionJpaEntity, "테스트 챕터", "테스트 챕터 설명", 1, new ArrayList<>(), now, now
        );
        em.persist(chapterJpaEntity);

        // 4. User 저장
        User noSectionProgressUser = new User(null, now, now, "userA", "userA@gmail.com", "유저A", "password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.PENDING);
        noSectionProgressUserJpaEntity = userJpaMapper.toJpaEntity(noSectionProgressUser);
        em.persist(noSectionProgressUserJpaEntity);

        User sectionProgressUser = new User(null, now, now, "userB", "userB@gmail.com", "유저B", "password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.PENDING);
        sectionProgressUserJpaEntity = userJpaMapper.toJpaEntity(sectionProgressUser);
        em.persist(sectionProgressUserJpaEntity);

        em.flush();

        // 5. UserSectionProgress 저장 (User, Section, Chapter 참조)
        UserSectionProgress sectionProgress = UserSectionProgress.start(
                sectionProgressUserJpaEntity.getId(), sectionJpaEntity.getId(), chapterJpaEntity.getId()
        );
        sectionProgress.completeFinalChapter();
        UserSectionProgressJpaEntity progressJpaEntity = userSectionProgressJpaMapper.toJpaEntity(
                sectionProgress, sectionProgressUserJpaEntity, sectionJpaEntity, chapterJpaEntity
        );
        em.persist(progressJpaEntity);

        em.flush();
    }

    @Test
    void findRankingsWithMyRank() {
        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(noSectionProgressUserJpaEntity.getId());
        Assertions.assertThat(rankingsWithMyRank).hasSize(2);
    }
}
