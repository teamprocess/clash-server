package com.process.clash.adapter.persistence.softdelete;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaMapper;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 유저 Soft Delete가 물리 삭제가 아님을 검증하는 통합 테스트.
 *
 * <p>JPA의 deleteById가 실제로 행을 제거하지 않고
 * deleted_at 컬럼만 갱신함을 확인한다.
 */
@DataJpaTest
@ActiveProfiles("test")
@Import({JpaAuditingConfig.class, UserJpaMapper.class})
class SoftDeleteIntegrationTest {

    @Autowired private TestEntityManager em;
    @Autowired private UserJpaRepository userJpaRepository;
    @Autowired private UserJpaMapper userJpaMapper;

    @Test
    @DisplayName("유저 soft delete 후 DB 행은 남아 있고 deleted_at이 설정된다")
    void userSoftDelete_rowSurvives_withDeletedAt() {
        UserJpaEntity user = persistUser("alice");
        Long userId = user.getId();

        userJpaRepository.deleteById(userId);
        em.flush();
        em.clear();

        // @SQLRestriction("deleted_at IS NULL")을 우회해 row 직접 확인
        Object[] row = (Object[]) em.getEntityManager()
            .createNativeQuery("SELECT id, deleted_at FROM users WHERE id = ?1")
            .setParameter(1, userId)
            .getSingleResult();

        assertThat(row[0]).isNotNull();  // row가 존재한다
        assertThat(row[1]).isNotNull();  // deleted_at이 채워졌다
    }

    @Test
    @DisplayName("유저 soft delete 후 findById는 결과를 반환하지 않는다")
    void userSoftDelete_findById_returnsEmpty() {
        UserJpaEntity user = persistUser("bob");
        Long userId = user.getId();

        userJpaRepository.deleteById(userId);
        em.flush();
        em.clear();

        Optional<UserJpaEntity> found = userJpaRepository.findById(userId);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("유저 soft delete 후 findByIdIncludingDeleted는 탈퇴된 유저를 반환한다")
    void userSoftDelete_findByIdIncludingDeleted_returnsUser() {
        UserJpaEntity user = persistUser("carol");
        Long userId = user.getId();

        userJpaRepository.deleteById(userId);
        em.flush();
        em.clear();

        Optional<UserJpaEntity> found = userJpaRepository.findByIdIncludingDeleted(userId);
        assertThat(found).isPresent();
        assertThat(found.get().getDeletedAt()).isNotNull();
        assertThat(found.get().getId()).isEqualTo(userId);
    }

    // ─── helpers ─────────────────────────────────────────────────────────────

    private int counter = 0;

    private UserJpaEntity persistUser(String label) {
        counter++;
        String suffix = label + counter;
        User user = new User(
            null, Instant.now(), Instant.now(),
            "user_" + suffix, suffix + "@test.com", "유저" + label,
            "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null
        );
        UserJpaEntity entity = userJpaMapper.toJpaEntity(user);
        em.persist(entity);
        em.flush();
        return entity;
    }
}
