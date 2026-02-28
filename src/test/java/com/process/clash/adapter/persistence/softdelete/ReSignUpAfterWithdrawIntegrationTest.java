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

@DataJpaTest
@ActiveProfiles("test")
@Import({JpaAuditingConfig.class, UserJpaMapper.class})
class ReSignUpAfterWithdrawIntegrationTest {

    @Autowired private TestEntityManager em;
    @Autowired private UserJpaRepository userJpaRepository;
    @Autowired private UserJpaMapper userJpaMapper;

    @Test
    @DisplayName("탈퇴한 유저의 username은 중복 체크에서 사용 가능으로 반환된다")
    void afterWithdraw_existsByUsername_returnsFalse() {
        UserJpaEntity user = persistUser("testuser", "test@example.com");
        userJpaRepository.deleteById(user.getId());
        em.flush();
        em.clear();

        boolean exists = userJpaRepository.existsByUsername("testuser");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("탈퇴한 유저의 email은 중복 체크에서 사용 가능으로 반환된다")
    void afterWithdraw_existsByEmail_returnsFalse() {
        UserJpaEntity user = persistUser("testuser", "test@example.com");
        userJpaRepository.deleteById(user.getId());
        em.flush();
        em.clear();

        boolean exists = userJpaRepository.existsByEmail("test@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("탈퇴 후 동일 username과 email로 재가입이 가능하다")
    void afterWithdraw_canReSignUpWithSameUsernameAndEmail() {
        // given: 기존 유저 가입 및 탈퇴
        UserJpaEntity original = persistUser("testuser", "test@example.com");
        Long originalId = original.getId();
        userJpaRepository.deleteById(originalId);
        em.flush();
        em.clear();

        // when: 동일 username/email로 새 유저 저장
        UserJpaEntity newEntity = userJpaMapper.toJpaEntity(new User(
                null, Instant.now(), Instant.now(),
                "testuser", "test@example.com", "재가입유저",
                "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null
        ));
        UserJpaEntity saved = userJpaRepository.save(newEntity);
        em.flush();
        em.clear();

        // then: 새 유저가 정상 저장됨
        Optional<UserJpaEntity> found = userJpaRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getDeletedAt()).isNull();
        assertThat(found.get().getId()).isNotEqualTo(originalId);
    }

    @Test
    @DisplayName("활성 유저의 username은 중복 체크에서 중복으로 반환된다")
    void activeUser_existsByUsername_returnsTrue() {
        persistUser("testuser", "test@example.com");
        em.flush();
        em.clear();

        boolean exists = userJpaRepository.existsByUsername("testuser");

        assertThat(exists).isTrue();
    }

    // ─── 추가 취약 시나리오 ────────────────────────────────────────────────────

    @Test
    @DisplayName("탈퇴한 유저는 findByUsername으로 조회되지 않는다 (로그인 차단)")
    void withdrawnUser_isNotFoundByUsername() {
        UserJpaEntity user = persistUser("testuser", "test@example.com");
        userJpaRepository.deleteById(user.getId());
        em.flush();
        em.clear();

        Optional<UserJpaEntity> found = userJpaRepository.findByUsername("testuser");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("탈퇴한 유저는 findByEmail로 조회되지 않는다 (로그인 차단)")
    void withdrawnUser_isNotFoundByEmail() {
        UserJpaEntity user = persistUser("testuser", "test@example.com");
        userJpaRepository.deleteById(user.getId());
        em.flush();
        em.clear();

        Optional<UserJpaEntity> found = userJpaRepository.findByEmail("test@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("탈퇴 유저 엔티티를 save() 재호출해도 deletedAt이 null로 초기화되지 않는다")
    void save_onWithdrawnUser_preservesDeletedAt() {
        // given: 유저 탈퇴
        UserJpaEntity user = persistUser("testuser", "test@example.com");
        Long userId = user.getId();
        userJpaRepository.deleteById(userId);
        em.flush();
        em.clear();

        // when: 탈퇴 유저 엔티티를 조회 후 save() 재호출 (관리자 작업 등)
        UserJpaEntity withdrawnEntity = userJpaRepository.findByIdIncludingDeleted(userId).get();
        userJpaRepository.save(withdrawnEntity);
        em.flush();
        em.clear();

        // then: deleted_at이 null로 덮어씌워지지 않아야 한다
        Object deletedAt = em.getEntityManager()
                .createNativeQuery("SELECT deleted_at FROM users WHERE id = ?1")
                .setParameter(1, userId)
                .getSingleResult();

        assertThat(deletedAt).isNotNull();
    }

    @Test
    @DisplayName("재가입 후 findByUsername은 신규 유저를 반환한다")
    void afterReSignUp_findByUsername_returnsNewUser() {
        // given: 탈퇴 후 재가입
        UserJpaEntity original = persistUser("testuser", "test@example.com");
        Long originalId = original.getId();
        userJpaRepository.deleteById(originalId);
        em.flush();
        em.clear();

        UserJpaEntity newEntity = userJpaMapper.toJpaEntity(new User(
                null, Instant.now(), Instant.now(),
                "testuser", "test@example.com", "재가입유저",
                "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null
        ));
        UserJpaEntity saved = userJpaRepository.save(newEntity);
        em.flush();
        em.clear();

        // when
        Optional<UserJpaEntity> found = userJpaRepository.findByUsername("testuser");

        // then: 기존 탈퇴 유저가 아닌 신규 유저가 반환된다
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getId()).isNotEqualTo(originalId);
        assertThat(found.get().getDeletedAt()).isNull();
    }

    // ─── helpers ──────────────────────────────────────────────────────────────

    private UserJpaEntity persistUser(String username, String email) {
        User user = new User(
                null, Instant.now(), Instant.now(),
                username, email, "테스트유저",
                "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null
        );
        UserJpaEntity entity = userJpaMapper.toJpaEntity(user);
        em.persist(entity);
        em.flush();
        return entity;
    }
}
