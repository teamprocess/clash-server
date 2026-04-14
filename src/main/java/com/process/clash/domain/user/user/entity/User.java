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
        boolean isPrivate,
        RankTier currentRankTier,
        ExpTier currentExpTier,
        int currentAttendanceStreak
) {
    public boolean isDeleted() {
        return deletedAt != null;
    }

    public String tier() {
        return RankTier.resolveTier(currentRankTier, currentExpTier);
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
                false,
                RankTier.NONE,
                ExpTier.UNRANKED,
                0
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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
        );
    }

    public User addCookie(int amount) {
        int nextTotalCookie = this.totalCookie + amount;

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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
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
                this.isPrivate,
                this.currentRankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
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
                this.isPrivate,
                this.currentRankTier,
                ExpTier.fromExp(newTotalExp),
                this.currentAttendanceStreak
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
                this.isPrivate,
                rankTier,
                this.currentExpTier,
                this.currentAttendanceStreak
        );
    }

    public User seasonReset(int cookieReward) {
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
                0,
                this.totalCookie + cookieReward,
                this.major,
                this.userStatus,
                this.deletedAt,
                this.isPrivate,
                RankTier.NONE,
                ExpTier.UNRANKED,
                this.currentAttendanceStreak
        );
    }

    public User updatePrivacy(boolean isPrivate) {
        return new User(
                this.id, this.createdAt, Instant.now(),
                this.username, this.email, this.name, this.password, this.role,
                this.profileImage, this.totalExp, this.totalCookie, this.major,
                this.userStatus, this.deletedAt, isPrivate, this.currentRankTier, this.currentExpTier,
                this.currentAttendanceStreak
        );
    }

    public User incrementAttendanceStreak() {
        return new User(
                this.id, this.createdAt, Instant.now(),
                this.username, this.email, this.name, this.password, this.role,
                this.profileImage, this.totalExp, this.totalCookie, this.major,
                this.userStatus, this.deletedAt, this.isPrivate, this.currentRankTier, this.currentExpTier,
                this.currentAttendanceStreak + 1
        );
    }

    public User resetAttendanceStreak() {
        return new User(
                this.id, this.createdAt, Instant.now(),
                this.username, this.email, this.name, this.password, this.role,
                this.profileImage, this.totalExp, this.totalCookie, this.major,
                this.userStatus, this.deletedAt, this.isPrivate, this.currentRankTier, this.currentExpTier,
                0
        );
    }
}
