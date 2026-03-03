package com.process.clash.domain.user.user.entity;

import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;

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
        Instant deletedAt
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
                null
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
                this.deletedAt
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
                this.deletedAt
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
                this.deletedAt
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
                this.deletedAt
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
                this.deletedAt
        );
    }

    public User addExp(int delta) {
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
                Math.max(0, this.totalExp + delta),
                this.totalCookie,
                this.major,
                this.userStatus,
                this.deletedAt
        );
    }
}
