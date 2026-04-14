package com.process.clash.adapter.persistence.roadmap.sectionprogress;

import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaEntity;
import com.process.clash.adapter.persistence.roadmap.category.CategoryJpaMapper;
import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaMapper;
import com.process.clash.adapter.persistence.roadmap.v2.choice.ChoiceV2JpaMapper;
import com.process.clash.adapter.persistence.roadmap.v2.questionhistory.UserQuestionHistoryV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.questionhistory.UserQuestionHistoryV2JpaMapper;
import com.process.clash.adapter.persistence.roadmap.v2.question.QuestionV2JpaMapper;
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
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import com.process.clash.infrastructure.config.JpaAuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

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
        ChapterV2JpaMapper.class,
        QuestionV2JpaMapper.class,
        ChoiceV2JpaMapper.class,
        UserQuestionHistoryV2JpaMapper.class,
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
    ChapterV2JpaEntity chapterJpaEntity;
    ChapterV2JpaEntity secondChapterJpaEntity;
    ChapterV2JpaEntity thirdChapterJpaEntity;
    SectionJpaEntity sectionJpaEntity;

    @BeforeEach
    void beforeEach() {
        Instant now = Instant.now();

        // 1. Category 저장
        CategoryJpaEntity categoryJpaEntity = new CategoryJpaEntity(null, "테스트 카테고리", null, now, now);
        em.persist(categoryJpaEntity);

        // 2. Section 저장 (Category 참조)
        sectionJpaEntity = new SectionJpaEntity(
                null, Major.SERVER, "테스트 섹션", "테스트 설명",
                categoryJpaEntity, 1, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), now, now
        );
        em.persist(sectionJpaEntity);

        // 3. Chapter 저장 (Section 참조)
        chapterJpaEntity = new ChapterV2JpaEntity(
                null, sectionJpaEntity, "테스트 챕터1", "테스트 챕터 설명", 1, null, new ArrayList<>(), now, now
        );
        em.persist(chapterJpaEntity);
        secondChapterJpaEntity = new ChapterV2JpaEntity(
                null, sectionJpaEntity, "테스트 챕터2", "테스트 챕터 설명", 2, null, new ArrayList<>(), now, now
        );
        em.persist(secondChapterJpaEntity);
        thirdChapterJpaEntity = new ChapterV2JpaEntity(
                null, sectionJpaEntity, "테스트 챕터3", "테스트 챕터 설명", 3, null, new ArrayList<>(), now, now
        );
        em.persist(thirdChapterJpaEntity);

        // 4. User 저장
        User noSectionProgressUser = new User(null, now, now, "userA", "userA@gmail.com", "유저A", "password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.PENDING, null, false, RankTier.NONE, ExpTier.UNRANKED, 0);
        noSectionProgressUserJpaEntity = userJpaMapper.toJpaEntity(noSectionProgressUser);
        em.persist(noSectionProgressUserJpaEntity);

        User sectionProgressUser = new User(null, now, now, "userB", "userB@gmail.com", "유저B", "password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.PENDING, null, false, RankTier.NONE, ExpTier.UNRANKED, 0);
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
    @DisplayName("실제 클리어 히스토리만 랭킹 점수로 집계된다")
    void findRankingsWithMyRank() {
        persistHistory(sectionProgressUserJpaEntity, chapterJpaEntity, true);
        em.flush();

        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(noSectionProgressUserJpaEntity.getId());
        Assertions.assertThat(rankingsWithMyRank).hasSize(2);

        Object[] firstRecord = rankingsWithMyRank.get(0);
        Assertions.assertThat(firstRecord).hasSize(7);
        Assertions.assertThat(actualClearedCount(firstRecord)).isEqualTo(1L);
        Assertions.assertThat(firstRecord[5]).isEqualTo(RankTier.NONE.name());
        Assertions.assertThat(firstRecord[6]).isEqualTo(ExpTier.UNRANKED.name());
    }

    @Test
    @DisplayName("user_section_progress.completedChapters 값은 랭킹 점수에 영향을 주지 않는다")
    void findRankingsWithMyRank_ignoresUserSectionProgressCompletedChapters() {
        UserSectionProgress inflatedProgress = new UserSectionProgress(
                null,
                noSectionProgressUserJpaEntity.getId(),
                sectionJpaEntity.getId(),
                chapterJpaEntity.getId(),
                999,
                false
        );
        UserSectionProgressJpaEntity inflatedProgressJpaEntity = userSectionProgressJpaMapper.toJpaEntity(
                inflatedProgress, noSectionProgressUserJpaEntity, sectionJpaEntity, chapterJpaEntity
        );
        em.persist(inflatedProgressJpaEntity);
        em.flush();

        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(noSectionProgressUserJpaEntity.getId());

        Object[] targetRecord = rankingsWithMyRank.stream()
                .filter(record -> record[0].equals(noSectionProgressUserJpaEntity.getId()))
                .findFirst()
                .orElseThrow();

        Assertions.assertThat(actualClearedCount(targetRecord)).isEqualTo(0L);
    }

    @Test
    @DisplayName("isCleared=false 히스토리는 랭킹 점수에 포함되지 않는다")
    void findRankingsWithMyRank_doesNotCountUnclearedHistory() {
        persistHistory(sectionProgressUserJpaEntity, chapterJpaEntity, false);
        em.flush();

        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(sectionProgressUserJpaEntity.getId());

        Object[] targetRecord = rankingsWithMyRank.stream()
                .filter(record -> record[0].equals(sectionProgressUserJpaEntity.getId()))
                .findFirst()
                .orElseThrow();

        Assertions.assertThat(actualClearedCount(targetRecord)).isEqualTo(0L);
    }

    @Test
    @DisplayName("여러 유저가 섞이면 실제 클리어 수 순으로 정렬된다")
    void findRankingsWithMyRank_ordersByActualClearedCount() {
        UserJpaEntity thirdUser = persistUser("userC", "유저C");

        persistHistory(sectionProgressUserJpaEntity, chapterJpaEntity, true);
        persistHistory(thirdUser, chapterJpaEntity, true);
        persistHistory(thirdUser, secondChapterJpaEntity, true);
        em.flush();

        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(noSectionProgressUserJpaEntity.getId());

        Assertions.assertThat(recordUserId(rankingsWithMyRank.get(0))).isEqualTo(thirdUser.getId());
        Assertions.assertThat(actualClearedCount(rankingsWithMyRank.get(0))).isEqualTo(2L);
        Assertions.assertThat(recordUserId(rankingsWithMyRank.get(1))).isEqualTo(sectionProgressUserJpaEntity.getId());
        Assertions.assertThat(actualClearedCount(rankingsWithMyRank.get(1))).isEqualTo(1L);
    }

    @Test
    @DisplayName("top20 밖 유저도 targetUserId로 조회하면 결과에 포함된다")
    void findRankingsWithMyRank_includesTargetUserOutsideTop20() {
        List<ChapterV2JpaEntity> chapters = new ArrayList<>();
        chapters.add(chapterJpaEntity);
        chapters.add(secondChapterJpaEntity);
        chapters.add(thirdChapterJpaEntity);

        IntStream.rangeClosed(4, 21).forEach(index -> chapters.add(persistAdditionalChapter(index)));

        List<UserJpaEntity> rankedUsers = new ArrayList<>();
        for (int score = 21; score >= 1; score--) {
            UserJpaEntity user = persistUser("ranked" + score, "랭커" + score);
            rankedUsers.add(user);
            for (int chapterIndex = 0; chapterIndex < score; chapterIndex++) {
                persistHistory(user, chapters.get(chapterIndex), true);
            }
        }
        em.flush();

        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(noSectionProgressUserJpaEntity.getId());

        Assertions.assertThat(rankingsWithMyRank).hasSize(21);
        Assertions.assertThat(rankingsWithMyRank.stream()
                .anyMatch(record -> recordUserId(record).equals(noSectionProgressUserJpaEntity.getId()))).isTrue();

        Object[] targetRecord = rankingsWithMyRank.stream()
                .filter(record -> recordUserId(record).equals(noSectionProgressUserJpaEntity.getId()))
                .findFirst()
                .orElseThrow();

        Assertions.assertThat(rankValue(targetRecord)).isEqualTo(22);
    }

    @Test
    @DisplayName("동점 유저는 동일한 rank 값을 가진다")
    void findRankingsWithMyRank_keepsSameRankWhenTied() {
        UserJpaEntity thirdUser = persistUser("userTie", "유저동점");

        persistHistory(sectionProgressUserJpaEntity, chapterJpaEntity, true);
        persistHistory(thirdUser, chapterJpaEntity, true);
        em.flush();

        List<Object[]> rankingsWithMyRank = userSectionProgressJpaRepository.findRankingsWithMyRank(noSectionProgressUserJpaEntity.getId());

        Object[] secondUserRecord = rankingsWithMyRank.stream()
                .filter(record -> recordUserId(record).equals(sectionProgressUserJpaEntity.getId()))
                .findFirst()
                .orElseThrow();
        Object[] thirdUserRecord = rankingsWithMyRank.stream()
                .filter(record -> recordUserId(record).equals(thirdUser.getId()))
                .findFirst()
                .orElseThrow();

        Assertions.assertThat(rankValue(secondUserRecord)).isEqualTo(1);
        Assertions.assertThat(rankValue(thirdUserRecord)).isEqualTo(1);
    }

    private UserJpaEntity persistUser(String username, String name) {
        Instant now = Instant.now();
        User user = new User(null, now, now, username, username + "@gmail.com", name, "password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.PENDING, null, false, RankTier.NONE, ExpTier.UNRANKED);
        UserJpaEntity entity = userJpaMapper.toJpaEntity(user);
        em.persist(entity);
        return entity;
    }

    private ChapterV2JpaEntity persistAdditionalChapter(int orderIndex) {
        ChapterV2JpaEntity chapter = new ChapterV2JpaEntity(
                null, sectionJpaEntity, "테스트 챕터" + orderIndex, "테스트 챕터 설명", orderIndex, null, new ArrayList<>(), Instant.now(), Instant.now()
        );
        em.persist(chapter);
        return chapter;
    }

    private void persistHistory(UserJpaEntity user, ChapterV2JpaEntity chapter, boolean isCleared) {
        em.persist(new UserQuestionHistoryV2JpaEntity(
                null,
                user,
                chapter,
                isCleared,
                isCleared ? 1 : 0,
                1,
                isCleared ? 1 : 0,
                null,
                null
        ));
    }

    private Long recordUserId(Object[] record) {
        return ((Number) record[0]).longValue();
    }

    private long actualClearedCount(Object[] record) {
        return ((Number) record[3]).longValue();
    }

    private int rankValue(Object[] record) {
        return ((Number) record[4]).intValue();
    }
}
