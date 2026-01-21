package com.process.clash.adapter.persistence.user.usergithub;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_github")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserGitHubJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String gitHubId;

    @Column
    private String githubUserNodeId;

    @Column(length = 2000)
    private String githubEmails;

    @Column
    private String githubAccessToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserJpaEntity user;

    public UserGitHubJpaEntity(Long id, String gitHubId, UserJpaEntity user) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.user = user;
    }
}
