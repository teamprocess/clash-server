package com.process.clash.adapter.persistence.user.usernotice;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import com.process.clash.infrastructure.config.JpaAuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({JpaAuditingConfig.class, UserJpaMapper.class, UserNoticeJpaMapper.class})
class UserNoticeJpaRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private UserNoticeJpaRepository userNoticeJpaRepository;
    @Autowired private UserJpaMapper userJpaMapper;

    private UserJpaEntity sender;
    private UserJpaEntity receiver;

    @BeforeEach
    void setUp() {
        sender = persistUser("sender", "sender@test.com");
        receiver = persistUser("receiver", "receiver@test.com");
    }

    @Test
    @DisplayName("APPLY_RIVAL soft delete 시 deleted_at과 is_read가 함께 설정된다")
    void softDeleteApplyRivalNotice_setsDeletedAtAndIsRead() {
        Long rivalId = 100L;
        Long noticeId = persistNotice(NoticeCategory.APPLY_RIVAL, rivalId, null).getId();

        userNoticeJpaRepository.softDeleteApplyRivalNoticeByRivalId(rivalId);
        em.flush();
        em.clear();

        Object[] row = (Object[]) em.getEntityManager()
                .createNativeQuery("SELECT deleted_at, is_read FROM user_notices WHERE id = ?1")
                .setParameter(1, noticeId)
                .getSingleResult();

        assertThat(row[0]).as("deleted_at이 설정되어야 한다").isNotNull();
        assertThat(row[1]).as("is_read가 true여야 한다").isEqualTo(true);
    }

    @Test
    @DisplayName("APPLY_BATTLE soft delete 시 deleted_at과 is_read가 함께 설정된다")
    void softDeleteApplyBattleNotice_setsDeletedAtAndIsRead() {
        Long battleId = 200L;
        Long noticeId = persistNotice(NoticeCategory.APPLY_BATTLE, null, battleId).getId();

        userNoticeJpaRepository.softDeleteApplyBattleNoticeByBattleId(battleId);
        em.flush();
        em.clear();

        Object[] row = (Object[]) em.getEntityManager()
                .createNativeQuery("SELECT deleted_at, is_read FROM user_notices WHERE id = ?1")
                .setParameter(1, noticeId)
                .getSingleResult();

        assertThat(row[0]).as("deleted_at이 설정되어야 한다").isNotNull();
        assertThat(row[1]).as("is_read가 true여야 한다").isEqualTo(true);
    }

    @Test
    @DisplayName("이미 읽은 APPLY_RIVAL 알림도 soft delete 시 deleted_at이 설정된다")
    void softDeleteApplyRivalNotice_alreadyRead_setsDeletedAt() {
        Long rivalId = 101L;
        UserNoticeJpaEntity notice = new UserNoticeJpaEntity(
                null, null, null, NoticeCategory.APPLY_RIVAL, true, false,
                sender, receiver, rivalId, null, null
        );
        em.persist(notice);
        em.flush();
        Long noticeId = notice.getId();

        userNoticeJpaRepository.softDeleteApplyRivalNoticeByRivalId(rivalId);
        em.flush();
        em.clear();

        Object[] row = (Object[]) em.getEntityManager()
                .createNativeQuery("SELECT deleted_at, is_read FROM user_notices WHERE id = ?1")
                .setParameter(1, noticeId)
                .getSingleResult();

        assertThat(row[0]).as("deleted_at이 설정되어야 한다").isNotNull();
        assertThat(row[1]).as("is_read는 true 유지").isEqualTo(true);
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private int counter = 0;

    private UserJpaEntity persistUser(String name, String email) {
        counter++;
        User user = new User(
                null, null, null,
                name + counter, email, name,
                "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null, RankTier.NONE, ExpTier.UNRANKED
        );
        UserJpaEntity entity = userJpaMapper.toJpaEntity(user);
        em.persist(entity);
        em.flush();
        return entity;
    }

    private UserNoticeJpaEntity persistNotice(NoticeCategory category, Long rivalId, Long battleId) {
        UserNoticeJpaEntity notice = new UserNoticeJpaEntity(
                null, null, null, category, false, false,
                sender, receiver, rivalId, battleId, null
        );
        em.persist(notice);
        em.flush();
        return notice;
    }
}
