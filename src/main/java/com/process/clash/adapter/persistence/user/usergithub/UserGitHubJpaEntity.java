package com.process.clash.adapter.persistence.user.usergithub;

import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_github")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGitHubJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String gitHubId;

    @Column(name = "github_user_node_id")
    private String githubUserNodeId;

    @Column(name = "github_emails", length = 2000)
    private String githubEmails;

    @Column(name = "github_access_token")
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
