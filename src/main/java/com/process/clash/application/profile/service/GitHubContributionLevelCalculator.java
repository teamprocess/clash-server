package com.process.clash.application.profile.service;

final class GitHubContributionLevelCalculator {

    private GitHubContributionLevelCalculator() {
    }

    static int fromCount(int count) {
        if (count <= 0) {
            return 0;
        }
        if (count <= 4) {
            return 1;
        }
        if (count <= 9) {
            return 2;
        }
        if (count <= 14) {
            return 3;
        }
        return 4;
    }
}
