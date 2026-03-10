package com.process.clash.domain.user.user.entity;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;

import java.time.Instant;

public record User(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String name,
        String password,
        Role role,
        String profileImage,
        int totalExp,
        int totalCookie,
        Major major,
        UserStatus userStatus,
        Instant deletedAt,
        RankTier currentRankTier,
        ExpTier currentExpTier
) {
    public boolean isDeleted() {
        return deletedAt != null;
    }

    public static User createDefault(String username, String email, String name, String password) {
        return new User(
                null,
                null,
                null,
                username,
                email,
                name,
                password,
                Role.USER,
                "",
                0,
                0,
                Major.NONE,
                UserStatus.PENDING,
                null,
                RankTier.NONE,
                ExpTier.UNRANKED
        );
    }

    public User submitMajor(Major major) {
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                major,
                this.userStatus,
                this.deletedAt,
                this.currentRankTier,
                this.currentExpTier
        );
    }

    public User active() {
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.major,
                UserStatus.ACTIVE,
                this.deletedAt,
                this.currentRankTier,
                this.currentExpTier
        );
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.userStatus);
    }

    public User updateSignupInfo(String username, String email, String name, String encodedPassword) {
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                username,
                email,
                name,
                encodedPassword,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.major,
                this.userStatus,
                this.deletedAt,
                this.currentRankTier,
                this.currentExpTier
        );
    }

    public User spendCookie(int amount) {
        int nextTotalCookie = this.totalCookie - amount;

        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                nextTotalCookie,
                this.major,
                this.userStatus,
                this.deletedAt,
                this.currentRankTier,
                this.currentExpTier
        );
    }

    public User updateProfileImage(String profileImage) {
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                profileImage,
                this.totalExp,
                this.totalCookie,
                this.major,
                this.userStatus,
                this.deletedAt,
                this.currentRankTier,
                this.currentExpTier
        );
    }

    public User withPassword(String encodedPassword) {
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                encodedPassword,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.major,
                this.userStatus,
                this.deletedAt,
                this.currentRankTier,
                this.currentExpTier
        );
    }

    public User addExp(int delta) {
        int newTotalExp = Math.max(0, this.totalExp + delta);
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                newTotalExp,
                this.totalCookie,
                this.major,
                this.userStatus,
                this.deletedAt,
                this.currentRankTier,
                ExpTier.fromExp(newTotalExp)
        );
    }

    public User withRankTier(RankTier rankTier) {
        return new User(
                this.id,
                this.createdAt,
                Instant.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.major,
                this.userStatus,
                this.deletedAt,
                rankTier,
                this.currentExpTier
        );
    }
}
