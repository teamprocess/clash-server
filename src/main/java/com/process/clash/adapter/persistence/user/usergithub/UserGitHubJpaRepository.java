package com.process.clash.adapter.persistence.user.usergithub;

import com.process.clash.application.compete.rival.data.AbleRivalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGitHubJpaRepository extends JpaRepository<UserGitHubJpaEntity, Long> {

    List<UserGitHubJpaEntity> findByUserIdNotIn(List<Long> ids);

    @Query("select ug from UserGitHubJpaEntity ug join fetch ug.user where ug.gitHubId is not null")
    List<UserGitHubJpaEntity> findAllWithUserAndGitHubId();

    @Query("""
        select new com.process.clash.application.compete.rival.data.AbleRivalInfo(
            u.id,
            u.name,
            ug.gitHubId,
            u.profileImage
        )
        from UserGitHubJpaEntity ug
        join ug.user u
        where u.id not in :excludedUserIds
    """)
    List<AbleRivalInfo> findAbleRivalsWithUserInfo(
            @Param("excludedUserIds") List<Long> excludedUserIds
    );

    @Query("""
        select new com.process.clash.application.compete.rival.data.AbleRivalInfo(
            u.id,
            u.name,
            ug.gitHubId,
            u.profileImage
        )
        from UserGitHubJpaEntity ug
        join ug.user u
        where u.id not in :excludedUserIds
            and (lower(ug.gitHubId) like lower(concat('%', :keyword, '%'))
                or lower(u.name) like lower(concat('%', :keyword, '%')))
    """)
    List<AbleRivalInfo> findAbleRivalsWithUserInfoByKeyword(
            @Param("excludedUserIds") List<Long> excludedUserIds,
            @Param("keyword") String keyword
    );
}
