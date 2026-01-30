package com.process.clash.adapter.persistence.user.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
	Optional<UserJpaEntity> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

    List<UserJpaEntity> findByIdIn(List<Long> ids);

	Optional<UserJpaEntity> findByEmail(String email);

	List<UserJpaEntity> findByIdIn(Set<Long> ids);

	@Query("""
        select new com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival(
            u.id,
            u.name,
            ug.gitHubId,
            u.profileImage
        )
        from UserJpaEntity u
        left join UserGitHubJpaEntity ug on u.id = ug.user.id
        where u.id not in :excludedUserIds
    """)
	List<AbleRivalInfoForRival> findAbleRivalsWithUserInfo(
			@Param("excludedUserIds") List<Long> excludedUserIds
	);
}
