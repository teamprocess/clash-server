package com.process.clash.adapter.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.application.github.exception.GithubRateLimitException;
import com.process.clash.application.github.exception.exception.internalserver.GithubGraphqlRequestFailedException;
import com.process.clash.application.github.exception.exception.internalserver.GithubGraphqlResponseErrorException;
import com.process.clash.application.github.model.GithubSyncTarget;
import com.process.clash.application.github.port.out.GithubStatsFetchPort;
import com.process.clash.application.github.service.GithubPullRequestSnapshotAggregator;
import com.process.clash.application.github.service.GithubReviewAggregator;
import com.process.clash.application.github.service.StudyDateCalculator;
import com.process.clash.domain.github.entity.GitHubDailyStats;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class GithubGraphqlAdapter implements GithubStatsFetchPort {

    private static final DateTimeFormatter INSTANT_FORMATTER = DateTimeFormatter.ISO_INSTANT;
    private static final int PAGE_SIZE = 100;

    private final WebClient githubWebClient;
    private final ObjectMapper objectMapper;
    private final GithubGraphqlQueries queries;
    private final StudyDateCalculator studyDateCalculator;
    private final GithubReviewAggregator reviewAggregator;
    private final GithubPullRequestSnapshotAggregator pullRequestSnapshotAggregator;
    private final Clock clock;

    public GithubGraphqlAdapter(
            @Qualifier("githubWebClient") WebClient githubWebClient,
            ObjectMapper objectMapper,
            GithubGraphqlQueries queries,
            StudyDateCalculator studyDateCalculator,
            GithubReviewAggregator reviewAggregator,
            GithubPullRequestSnapshotAggregator pullRequestSnapshotAggregator,
            Clock clock
    ) {
        this.githubWebClient = githubWebClient;
        this.objectMapper = objectMapper;
        this.queries = queries;
        this.studyDateCalculator = studyDateCalculator;
        this.reviewAggregator = reviewAggregator;
        this.pullRequestSnapshotAggregator = pullRequestSnapshotAggregator;
        this.clock = clock;
    }

    @Override
    public List<GitHubDailyStats> fetchDailyStats(GithubSyncTarget target, List<LocalDate> studyDates) {
        if (studyDates.isEmpty()) {
            return List.of();
        }

        List<LocalDate> orderedStudyDates = studyDates.stream()
                .sorted()
                .toList();

        // 전체 기간을 먼저 계산해 공통 질의 범위를 재사용
        LocalDate startDate = orderedStudyDates.getFirst();
        LocalDate endDate = orderedStudyDates.getLast();
        Instant rangeStart = studyDateCalculator.rangeStartUtc(startDate);
        Instant rangeEndExclusive = studyDateCalculator.rangeEndExclusiveUtc(endDate);
        Instant rangeEndInclusive = rangeEndExclusive.minusMillis(1);

        // 날짜별 집계를 위한 초기 버킷 생성
        Map<LocalDate, MutableStats> statsByDate = initializeStats(orderedStudyDates);
        Set<LocalDate> studyDateSet = statsByDate.keySet();

        // 리뷰/커밋은 전체 기간을 조회한 뒤 날짜별로 집계
        Map<LocalDate, Integer> reviewCounts = fetchReviewCounts(target, rangeStart, rangeEndInclusive);
        mergeReviewCounts(statsByDate, reviewCounts);

        Map<LocalDate, CommitStats> commitStats = fetchCommitStats(target, rangeStart, rangeEndInclusive, studyDateSet);
        mergeCommitStats(statsByDate, commitStats);

        // PR 생성/상태 지표 수집
        PullRequestStats pullRequestStats = fetchPullRequestStats(
                target,
                orderedStudyDates,
                rangeStart,
                rangeEndInclusive,
                studyDateSet
        );
        mergePullRequestStats(statsByDate, pullRequestStats);

        // 이슈는 createdAt 기준으로 날짜별 집계
        Map<LocalDate, Integer> issueCounts = fetchSearchCountsByDate(
                target,
                "is:issue",
                "created",
                "createdAt",
                rangeStart,
                rangeEndInclusive,
                studyDateSet
        );

        for (LocalDate studyDate : orderedStudyDates) {
            MutableStats mutable = statsByDate.get(studyDate);
            mutable.issueCount = issueCounts.getOrDefault(studyDate, 0);
        }

        Instant syncedAt = clock.instant();
        List<GitHubDailyStats> results = new ArrayList<>(orderedStudyDates.size());
        for (LocalDate studyDate : orderedStudyDates) {
            MutableStats mutable = statsByDate.get(studyDate);
            results.add(new GitHubDailyStats(
                    target.userId(),
                    studyDate,
                    mutable.commitCount,
                    mutable.prCount,
                    mutable.issueCount,
                    mutable.reviewedPrCount,
                    mutable.additions,
                    mutable.deletions,
                    mutable.topCommitRepo,
                    mutable.topPrRepo,
                    mutable.firstCommitAt,
                    mutable.lastCommitAt,
                    mutable.prMergedCount,
                    mutable.prOpenCount,
                    mutable.prClosedCount,
                    syncedAt
            ));
        }
        return results;
    }

    private Map<LocalDate, MutableStats> initializeStats(List<LocalDate> studyDates) {
        Map<LocalDate, MutableStats> statsByDate = new HashMap<>();
        // 모든 날짜를 미리 등록해 누락 없이 집계될 수 있도록 초기화
        for (LocalDate date : studyDates) {
            statsByDate.put(date, new MutableStats());
        }
        return statsByDate;
    }

    private PullRequestStats fetchPullRequestStats(
            GithubSyncTarget target,
            List<LocalDate> orderedStudyDates,
            Instant rangeStart,
            Instant rangeEndInclusive,
            Set<LocalDate> studyDates
    ) {
        PullRequestCreatedStats createdStats = fetchPullRequestCreatedStatsByDate(
                target,
                rangeStart,
                rangeEndInclusive,
                orderedStudyDates,
                studyDates
        );

        Map<LocalDate, Integer> mergedCounts = fetchSearchCountsByDate(
                target,
                "is:pr is:merged",
                "merged",
                "mergedAt",
                rangeStart,
                rangeEndInclusive,
                studyDates
        );

        Map<LocalDate, Integer> closedCounts = fetchSearchCountsByDate(
                target,
                "is:pr is:closed -is:merged",
                "closed",
                "closedAt",
                rangeStart,
                rangeEndInclusive,
                studyDates
        );

        // Open 지표는 "해당 학습일에 생성된 PR 수"로 계산한다.
        Map<LocalDate, Integer> openCounts = pullRequestSnapshotAggregator.calculateOpenCounts(
                orderedStudyDates,
                createdStats.countsByDate
        );

        Map<LocalDate, String> topPrRepoByDate = new HashMap<>();
        for (Map.Entry<LocalDate, Map<String, Integer>> entry : createdStats.repoCountsByDate.entrySet()) {
            String topRepo = pullRequestSnapshotAggregator.selectTopRepository(entry.getValue());
            if (topRepo != null) {
                topPrRepoByDate.put(entry.getKey(), topRepo);
            }
        }

        return new PullRequestStats(
                createdStats.countsByDate,
                mergedCounts,
                closedCounts,
                openCounts,
                topPrRepoByDate
        );
    }

    private PullRequestCreatedStats fetchPullRequestCreatedStatsByDate(
            GithubSyncTarget target,
            Instant rangeStart,
            Instant rangeEndInclusive,
            List<LocalDate> orderedStudyDates,
            Set<LocalDate> studyDates
    ) {
        Map<LocalDate, Integer> counts = new HashMap<>();
        Map<LocalDate, Map<String, Integer>> repoCountsByDate = new HashMap<>();

        String queryValue = buildRangeQuery(target, "is:pr", "created", rangeStart, rangeEndInclusive);

        String cursor = null;
        boolean hasNext = true;
        boolean firstPage = true;

        while (hasNext) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("query", queryValue);
            variables.put("first", PAGE_SIZE);
            variables.put("after", cursor);

            JsonNode data = executeQuery(target, "search-issue-nodes", variables);
            JsonNode search = data.path("search");

            if (firstPage) {
                firstPage = false;
                int totalCount = search.path("issueCount").asInt(0);
                // GitHub Search는 1000건 제한이 있어 초과 시 날짜별 fallback으로 전환
                if (totalCount > 1000) {
                    return fetchPullRequestCreatedStatsByDateFallback(target, orderedStudyDates);
                }
            }

            JsonNode nodes = search.path("nodes");
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    LocalDate studyDate = toStudyDate(node, "createdAt", studyDates);
                    if (studyDate == null) {
                        continue;
                    }

                    counts.merge(studyDate, 1, Integer::sum);

                    String repositoryName = node.path("repository").path("nameWithOwner").asText(null);
                    if (repositoryName != null && !repositoryName.isBlank()) {
                        repoCountsByDate
                                .computeIfAbsent(studyDate, key -> new HashMap<>())
                                .merge(repositoryName, 1, Integer::sum);
                    }
                }
            }

            JsonNode pageInfo = search.path("pageInfo");
            hasNext = pageInfo.path("hasNextPage").asBoolean(false);
            cursor = pageInfo.path("endCursor").asText(null);
        }

        return new PullRequestCreatedStats(counts, repoCountsByDate);
    }

    private PullRequestCreatedStats fetchPullRequestCreatedStatsByDateFallback(
            GithubSyncTarget target,
            List<LocalDate> orderedStudyDates
    ) {
        Map<LocalDate, Integer> counts = new HashMap<>();
        Map<LocalDate, Map<String, Integer>> repoCountsByDate = new HashMap<>();

        for (LocalDate studyDate : orderedStudyDates) {
            Instant startUtc = studyDateCalculator.rangeStartUtc(studyDate);
            Instant endUtcExclusive = studyDateCalculator.rangeEndExclusiveUtc(studyDate);
            Instant endUtcInclusive = endUtcExclusive.minusMillis(1);
            String queryValue = buildRangeQuery(target, "is:pr", "created", startUtc, endUtcInclusive);

            SearchNodesByDateResult dayResult = fetchSearchNodesByDate(target, queryValue, "createdAt", Set.of(studyDate));

            counts.put(studyDate, dayResult.totalCount);

            Map<String, Integer> dayRepoCounts = dayResult.repoCountsByDate.get(studyDate);
            if (dayRepoCounts != null && !dayRepoCounts.isEmpty()) {
                repoCountsByDate.put(studyDate, dayRepoCounts);
            }

            // 하루 단위에서도 1000건이 초과되면 대표 레포는 일부 노드(최대 1000건) 기준으로 근사
            if (dayResult.totalCount > 1000) {
                log.warn(
                        "PR 생성 검색 결과가 하루 1000건 제한을 초과했습니다. 대표 레포 정확도가 낮아질 수 있습니다. userId={}, date={}, totalCount={}",
                        target.userId(),
                        studyDate,
                        dayResult.totalCount
                );
            }
        }

        return new PullRequestCreatedStats(counts, repoCountsByDate);
    }

    private int fetchSearchCount(
            GithubSyncTarget target,
            String queryValue
    ) {
        Map<String, Object> variables = Map.of("query", queryValue);
        JsonNode data = executeQuery(target, "search-issue-count", variables);
        return data.path("search").path("issueCount").asInt(0);
    }

    private Map<LocalDate, Integer> fetchSearchCountsByDate(
            GithubSyncTarget target,
            String typeQualifier,
            String dateQualifier,
            String timestampField,
            Instant rangeStart,
            Instant rangeEndInclusive,
            Set<LocalDate> studyDates
    ) {
        Map<LocalDate, Integer> counts = new HashMap<>();

        String queryValue = buildRangeQuery(target, typeQualifier, dateQualifier, rangeStart, rangeEndInclusive);

        String cursor = null;
        boolean hasNext = true;
        boolean firstPage = true;

        while (hasNext) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("query", queryValue);
            variables.put("first", PAGE_SIZE);
            variables.put("after", cursor);

            JsonNode data = executeQuery(target, "search-issue-nodes", variables);
            JsonNode search = data.path("search");

            if (firstPage) {
                firstPage = false;
                int totalCount = search.path("issueCount").asInt(0);
                // GitHub Search는 1000건 제한이 있어 초과 시 날짜별 count fallback으로 정확도 보장
                if (totalCount > 1000) {
                    return fetchSearchCountsByDateFallback(target, typeQualifier, dateQualifier, studyDates);
                }
            }

            JsonNode nodes = search.path("nodes");
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    LocalDate studyDate = toStudyDate(node, timestampField, studyDates);
                    if (studyDate == null) {
                        continue;
                    }
                    counts.merge(studyDate, 1, Integer::sum);
                }
            }

            JsonNode pageInfo = search.path("pageInfo");
            hasNext = pageInfo.path("hasNextPage").asBoolean(false);
            cursor = pageInfo.path("endCursor").asText(null);
        }

        return counts;
    }

    private Map<LocalDate, Integer> fetchSearchCountsByDateFallback(
            GithubSyncTarget target,
            String typeQualifier,
            String dateQualifier,
            Set<LocalDate> studyDates
    ) {
        Map<LocalDate, Integer> counts = new HashMap<>();
        // 날짜별 카운트로 정확도 보장 (API 호출 수는 증가)
        for (LocalDate studyDate : studyDates) {
            Instant startUtc = studyDateCalculator.rangeStartUtc(studyDate);
            Instant endUtcExclusive = studyDateCalculator.rangeEndExclusiveUtc(studyDate);
            Instant endUtcInclusive = endUtcExclusive.minusMillis(1);

            String queryValue = buildRangeQuery(target, typeQualifier, dateQualifier, startUtc, endUtcInclusive);
            int count = fetchSearchCount(target, queryValue);
            counts.put(studyDate, count);
        }
        return counts;
    }

    private SearchNodesByDateResult fetchSearchNodesByDate(
            GithubSyncTarget target,
            String queryValue,
            String timestampField,
            Set<LocalDate> studyDates
    ) {
        Map<LocalDate, Integer> countsByDate = new HashMap<>();
        Map<LocalDate, Map<String, Integer>> repoCountsByDate = new HashMap<>();

        int totalCount = 0;
        String cursor = null;
        boolean hasNext = true;
        boolean firstPage = true;

        while (hasNext) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("query", queryValue);
            variables.put("first", PAGE_SIZE);
            variables.put("after", cursor);

            JsonNode data = executeQuery(target, "search-issue-nodes", variables);
            JsonNode search = data.path("search");

            if (firstPage) {
                firstPage = false;
                totalCount = search.path("issueCount").asInt(0);
            }

            JsonNode nodes = search.path("nodes");
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    LocalDate studyDate = toStudyDate(node, timestampField, studyDates);
                    if (studyDate == null) {
                        continue;
                    }

                    countsByDate.merge(studyDate, 1, Integer::sum);

                    String repositoryName = node.path("repository").path("nameWithOwner").asText(null);
                    if (repositoryName != null && !repositoryName.isBlank()) {
                        repoCountsByDate
                                .computeIfAbsent(studyDate, key -> new HashMap<>())
                                .merge(repositoryName, 1, Integer::sum);
                    }
                }
            }

            JsonNode pageInfo = search.path("pageInfo");
            hasNext = pageInfo.path("hasNextPage").asBoolean(false);
            cursor = pageInfo.path("endCursor").asText(null);
        }

        return new SearchNodesByDateResult(totalCount, countsByDate, repoCountsByDate);
    }

    private String buildRangeQuery(
            GithubSyncTarget target,
            String typeQualifier,
            String dateQualifier,
            Instant startUtc,
            Instant endInclusive
    ) {
        return String.format(
                Locale.ROOT,
                "%s author:%s %s:%s..%s",
                typeQualifier,
                target.githubLogin(),
                dateQualifier,
                INSTANT_FORMATTER.format(startUtc),
                INSTANT_FORMATTER.format(endInclusive)
        );
    }

    private LocalDate toStudyDate(JsonNode node, String timestampField, Set<LocalDate> studyDates) {
        String timestampRaw = node.path(timestampField).asText(null);
        if (timestampRaw == null || timestampRaw.isBlank()) {
            return null;
        }
        LocalDate studyDate = studyDateCalculator.toStudyDate(Instant.parse(timestampRaw));
        if (!studyDates.contains(studyDate)) {
            return null;
        }
        return studyDate;
    }

    private Map<LocalDate, Integer> fetchReviewCounts(GithubSyncTarget target, Instant rangeStart, Instant rangeEndInclusive) {
        List<GithubReviewAggregator.ReviewContribution> contributions = new ArrayList<>();
        String cursor = null;
        boolean hasNext = true;

        // review-contributions는 pagination이 필수
        while (hasNext) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("login", target.githubLogin());
            variables.put("from", INSTANT_FORMATTER.format(rangeStart));
            variables.put("to", INSTANT_FORMATTER.format(rangeEndInclusive));
            variables.put("first", PAGE_SIZE);
            variables.put("after", cursor);

            JsonNode data = executeQuery(target, "review-contributions", variables);
            JsonNode connection = data.path("user").path("contributionsCollection")
                    .path("pullRequestReviewContributions");
            JsonNode nodes = connection.path("nodes");
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    String occurredAt = node.path("occurredAt").asText(null);
                    String prId = node.path("pullRequest").path("id").asText(null);
                    // 리뷰가 발생한 시각과 PR ID로 중복 제거 집계
                    if (occurredAt != null && prId != null) {
                        contributions.add(new GithubReviewAggregator.ReviewContribution(
                                Instant.parse(occurredAt), prId
                        ));
                    }
                }
            }

            JsonNode pageInfo = connection.path("pageInfo");
            hasNext = pageInfo.path("hasNextPage").asBoolean(false);
            cursor = pageInfo.path("endCursor").asText(null);
        }

        return reviewAggregator.countDistinctReviewedPrsByStudyDate(contributions, studyDateCalculator);
    }

    private Map<LocalDate, CommitStats> fetchCommitStats(
            GithubSyncTarget target,
            Instant rangeStart,
            Instant rangeEndInclusive,
            Set<LocalDate> studyDates
    ) {
        Map<LocalDate, CommitStats> stats = new HashMap<>();
        // 기여한 레포 전체를 순회하며 커밋 히스토리를 집계
        List<RepoInfo> repos = fetchRepositories(target);
        for (RepoInfo repo : repos) {
            accumulateRepoCommits(target, repo, rangeStart, rangeEndInclusive, studyDates, stats);
        }
        return stats;
    }

    private List<RepoInfo> fetchRepositories(GithubSyncTarget target) {
        List<RepoInfo> repos = new ArrayList<>();
        String cursor = null;
        boolean hasNext = true;

        // repositoriesContributedTo 연결을 모두 순회
        while (hasNext) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("login", target.githubLogin());
            variables.put("first", PAGE_SIZE);
            variables.put("after", cursor);
            JsonNode data = executeQuery(target, "repos-contributed", variables);
            JsonNode connection = data.path("user").path("repositoriesContributedTo");
            JsonNode nodes = connection.path("nodes");
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    String nameWithOwner = node.path("nameWithOwner").asText(null);
                    String defaultBranchName = node.path("defaultBranchRef").path("name").asText(null);
                    if (nameWithOwner != null && defaultBranchName != null) {
                        repos.add(new RepoInfo(nameWithOwner));
                    }
                }
            }
            JsonNode pageInfo = connection.path("pageInfo");
            hasNext = pageInfo.path("hasNextPage").asBoolean(false);
            cursor = pageInfo.path("endCursor").asText(null);
        }

        return repos;
    }

    private void accumulateRepoCommits(
            GithubSyncTarget target,
            RepoInfo repo,
            Instant rangeStart,
            Instant rangeEndInclusive,
            Set<LocalDate> studyDates,
            Map<LocalDate, CommitStats> stats
    ) {
        String[] parts = repo.nameWithOwner().split("/");
        // nameWithOwner 형식이 아니면 스킵
        if (parts.length != 2) {
            return;
        }
        String owner = parts[0];
        String name = parts[1];

        String cursor = null;
        boolean hasNext = true;
        // 레포별 커밋 히스토리를 모두 순회하며 날짜별 누적
        while (hasNext) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("owner", owner);
            variables.put("name", name);
            variables.put("first", PAGE_SIZE);
            variables.put("after", cursor);
            variables.put("since", INSTANT_FORMATTER.format(rangeStart));
            variables.put("until", INSTANT_FORMATTER.format(rangeEndInclusive));
            variables.put("author", buildCommitAuthor(target));

            JsonNode data = executeQuery(target, "repo-commit-history", variables);
            JsonNode history = data.path("repository")
                    .path("defaultBranchRef")
                    .path("target")
                    .path("history");

            JsonNode nodes = history.path("nodes");
            if (nodes.isArray()) {
                for (JsonNode node : nodes) {
                    String committedDateRaw = node.path("committedDate").asText(null);
                    if (committedDateRaw == null) {
                        continue;
                    }
                    Instant committedAt = Instant.parse(committedDateRaw);
                    LocalDate studyDate = studyDateCalculator.toStudyDate(committedAt);
                    // 대상 기간 외 커밋은 제외
                    if (!studyDates.contains(studyDate)) {
                        continue;
                    }
                    CommitStats commitStats = stats.computeIfAbsent(studyDate, key -> new CommitStats());
                    commitStats.commitCount += 1;
                    commitStats.additions += node.path("additions").asLong(0);
                    commitStats.deletions += node.path("deletions").asLong(0);
                    commitStats.repositoryCommitCounts.merge(repo.nameWithOwner(), 1, Integer::sum);

                    if (commitStats.firstCommitAt == null || committedAt.isBefore(commitStats.firstCommitAt)) {
                        commitStats.firstCommitAt = committedAt;
                    }
                    if (commitStats.lastCommitAt == null || committedAt.isAfter(commitStats.lastCommitAt)) {
                        commitStats.lastCommitAt = committedAt;
                    }
                }
            }

            JsonNode pageInfo = history.path("pageInfo");
            hasNext = pageInfo.path("hasNextPage").asBoolean(false);
            cursor = pageInfo.path("endCursor").asText(null);
        }
    }

    private Map<String, Object> buildCommitAuthor(GithubSyncTarget target) {
        Map<String, Object> author = new HashMap<>();
        // 가능한 식별 정보(id/emails)를 활용해 커밋 작성자 필터링
        if (target.githubUserNodeId() != null && !target.githubUserNodeId().isBlank()) {
            author.put("id", target.githubUserNodeId());
        }
        if (target.emails() != null && !target.emails().isEmpty()) {
            author.put("emails", target.emails());
        }
        return author.isEmpty() ? null : author;
    }

    private void mergeReviewCounts(Map<LocalDate, MutableStats> statsByDate, Map<LocalDate, Integer> reviewCounts) {
        // 리뷰 집계를 기존 통계 버킷에 합산
        for (Map.Entry<LocalDate, Integer> entry : reviewCounts.entrySet()) {
            MutableStats mutable = statsByDate.get(entry.getKey());
            if (mutable != null) {
                mutable.reviewedPrCount = entry.getValue();
            }
        }
    }

    private void mergeCommitStats(Map<LocalDate, MutableStats> statsByDate, Map<LocalDate, CommitStats> commitStats) {
        // 커밋 집계를 기존 통계 버킷에 합산
        for (Map.Entry<LocalDate, CommitStats> entry : commitStats.entrySet()) {
            MutableStats mutable = statsByDate.get(entry.getKey());
            if (mutable != null) {
                CommitStats stats = entry.getValue();
                mutable.commitCount = stats.commitCount;
                mutable.additions = stats.additions;
                mutable.deletions = stats.deletions;
                mutable.firstCommitAt = stats.firstCommitAt;
                mutable.lastCommitAt = stats.lastCommitAt;
                mutable.topCommitRepo = pullRequestSnapshotAggregator.selectTopRepository(stats.repositoryCommitCounts);
            }
        }
    }

    private void mergePullRequestStats(Map<LocalDate, MutableStats> statsByDate, PullRequestStats pullRequestStats) {
        for (Map.Entry<LocalDate, MutableStats> entry : statsByDate.entrySet()) {
            LocalDate studyDate = entry.getKey();
            MutableStats mutable = entry.getValue();

            mutable.prCount = pullRequestStats.createdCountsByDate.getOrDefault(studyDate, 0);
            mutable.prMergedCount = pullRequestStats.mergedCountsByDate.getOrDefault(studyDate, 0);
            mutable.prClosedCount = pullRequestStats.closedCountsByDate.getOrDefault(studyDate, 0);
            mutable.prOpenCount = pullRequestStats.openCountsByDate.getOrDefault(studyDate, 0);
            mutable.topPrRepo = pullRequestStats.topRepositoryByDate.get(studyDate);
        }
    }

    private JsonNode executeQuery(GithubSyncTarget target, String queryName, Map<String, Object> variables) {
        String query = queries.get(queryName);
        Map<String, Object> request = new HashMap<>();
        request.put("query", query);
        request.put("variables", variables);

        // 네트워크/일시적 서버 오류만 제한적으로 재시도
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                String response = githubWebClient.post()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + target.accessToken())
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                JsonNode root = objectMapper.readTree(response);
                handleGraphqlErrors(root, target, queryName);
                JsonNode data = root.path("data");
                logRateLimit(data, queryName, target.userId());
                return data;
            } catch (GithubRateLimitException ex) {
                throw ex;
            } catch (WebClientResponseException ex) {
                // 5xx만 재시도하고 나머지는 즉시 실패 처리
                if (ex.getStatusCode().is5xxServerError() && attempt < 3) {
                    backoff(attempt);
                    continue;
                }
                throw ex;
            } catch (WebClientRequestException ex) {
                if (attempt < 3) {
                    backoff(attempt);
                    continue;
                }
                throw ex;
            } catch (IOException ex) {
                // 응답 파싱 오류 등은 제한된 재시도로 복구 시도
                if (attempt < 3) {
                    backoff(attempt);
                    continue;
                }
                throw new GithubGraphqlRequestFailedException(ex);
            }
        }
        throw new GithubGraphqlRequestFailedException();
    }

    private void handleGraphqlErrors(JsonNode root, GithubSyncTarget target, String queryName) {
        JsonNode errors = root.path("errors");
        if (errors.isArray() && errors.size() > 0) {
            List<String> messages = new ArrayList<>();
            for (JsonNode error : errors) {
                String message = error.path("message").asText("");
                messages.add(message);
            }
            String combined = String.join(" | ", messages);
            // Rate limit/abuse는 별도 예외로 분기
            if (combined.toLowerCase(Locale.ROOT).contains("rate limit") ||
                    combined.toLowerCase(Locale.ROOT).contains("abuse")) {
                Instant resetAt = parseRateLimitResetAt(root.path("data"));
                throw new GithubRateLimitException("GitHub 요청 제한에 도달했습니다: " + combined, resetAt);
            }
            throw new GithubGraphqlResponseErrorException();
        }
    }

    private Instant parseRateLimitResetAt(JsonNode data) {
        String resetAt = data.path("rateLimit").path("resetAt").asText(null);
        if (resetAt == null) {
            return null;
        }
        return Instant.parse(resetAt);
    }

    private void logRateLimit(JsonNode data, String queryName, Long userId) {
        JsonNode rateLimit = data.path("rateLimit");
        if (!rateLimit.isMissingNode()) {
            log.debug("GitHub 요청 제한 상태. query={}, userId={}, cost={}, remaining={}, resetAt={}",
                    queryName,
                    userId,
                    rateLimit.path("cost").asInt(),
                    rateLimit.path("remaining").asInt(),
                    rateLimit.path("resetAt").asText(null)
            );
        }
    }

    private void backoff(int attempt) {
        try {
            long delay = (long) (500L * Math.pow(2, attempt - 1));
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static class MutableStats {
        private int commitCount;
        private int prCount;
        private int issueCount;
        private int reviewedPrCount;
        private long additions;
        private long deletions;
        private String topCommitRepo;
        private String topPrRepo;
        private Instant firstCommitAt;
        private Instant lastCommitAt;
        private int prMergedCount;
        private int prOpenCount;
        private int prClosedCount;
    }

    private static class CommitStats {
        private int commitCount;
        private long additions;
        private long deletions;
        private Instant firstCommitAt;
        private Instant lastCommitAt;
        private final Map<String, Integer> repositoryCommitCounts = new HashMap<>();
    }

    private static class PullRequestCreatedStats {
        private final Map<LocalDate, Integer> countsByDate;
        private final Map<LocalDate, Map<String, Integer>> repoCountsByDate;

        private PullRequestCreatedStats(
                Map<LocalDate, Integer> countsByDate,
                Map<LocalDate, Map<String, Integer>> repoCountsByDate
        ) {
            this.countsByDate = countsByDate;
            this.repoCountsByDate = repoCountsByDate;
        }
    }

    private static class PullRequestStats {
        private final Map<LocalDate, Integer> createdCountsByDate;
        private final Map<LocalDate, Integer> mergedCountsByDate;
        private final Map<LocalDate, Integer> closedCountsByDate;
        private final Map<LocalDate, Integer> openCountsByDate;
        private final Map<LocalDate, String> topRepositoryByDate;

        private PullRequestStats(
                Map<LocalDate, Integer> createdCountsByDate,
                Map<LocalDate, Integer> mergedCountsByDate,
                Map<LocalDate, Integer> closedCountsByDate,
                Map<LocalDate, Integer> openCountsByDate,
                Map<LocalDate, String> topRepositoryByDate
        ) {
            this.createdCountsByDate = createdCountsByDate;
            this.mergedCountsByDate = mergedCountsByDate;
            this.closedCountsByDate = closedCountsByDate;
            this.openCountsByDate = openCountsByDate;
            this.topRepositoryByDate = topRepositoryByDate;
        }
    }

    private static class SearchNodesByDateResult {
        private final int totalCount;
        private final Map<LocalDate, Integer> countsByDate;
        private final Map<LocalDate, Map<String, Integer>> repoCountsByDate;

        private SearchNodesByDateResult(
                int totalCount,
                Map<LocalDate, Integer> countsByDate,
                Map<LocalDate, Map<String, Integer>> repoCountsByDate
        ) {
            this.totalCount = totalCount;
            this.countsByDate = countsByDate;
            this.repoCountsByDate = repoCountsByDate;
        }
    }

    private record RepoInfo(String nameWithOwner) {
    }
}
