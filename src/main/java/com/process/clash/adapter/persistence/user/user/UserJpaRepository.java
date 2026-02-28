package com.process.clash.adapter.persistence.user.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.LockModeType;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    @Query("select u from UserJpaEntity u where u.username = :username and u.deletedAt is null")
	Optional<UserJpaEntity> findByUsername(@Param("username") String username);

    @Query("select count(u) > 0 from UserJpaEntity u where u.username = :username and u.deletedAt is null")
	boolean existsByUsername(@Param("username") String username);

    @Query("select count(u) > 0 from UserJpaEntity u where u.email = :email and u.deletedAt is null")
	boolean existsByEmail(@Param("email") String email);

    List<UserJpaEntity> findByIdIn(List<Long> ids);

    @Query("select u from UserJpaEntity u where u.email = :email and u.deletedAt is null")
	Optional<UserJpaEntity> findByEmail(@Param("email") String email);

	List<UserJpaEntity> findByIdIn(Set<Long> ids);

	@Query("""
        select new com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForRival(
            u.id,
            u.username,
            u.name,
            ug.gitHubId,
            u.profileImage
        )
        from UserJpaEntity u
        left join UserGitHubJpaEntity ug on u.id = ug.user.id
        where u.id not in :excludedUserIds
          and u.deletedAt is null
    """)
	List<AbleRivalInfoForRival> findAbleRivalsWithUserInfo(
			@Param("excludedUserIds") List<Long> excludedUserIds
	);

    @Override
    @Query("select u from UserJpaEntity u where u.id = :id and u.deletedAt is null")
    Optional<UserJpaEntity> findById(@Param("id") Long id);

    @Query("select u from UserJpaEntity u where u.id = :id")
    Optional<UserJpaEntity> findByIdIncludingDeleted(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserJpaEntity u where u.id = :id and u.deletedAt is null")
    Optional<UserJpaEntity> findByIdForUpdate(@Param("id") Long id);
}
