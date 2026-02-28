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
