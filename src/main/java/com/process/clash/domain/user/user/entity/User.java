package com.process.clash.domain.user.user.entity;

import com.process.clash.domain.common.enums.GoodsType;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;

import java.time.LocalDateTime;

public record User(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String email,
        String name,
        String password,
        Role role,
        String profileImage,
        int totalExp,
        int totalCookie,
        int totalToken,
        Major major,
        UserStatus userStatus
) {
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
                0,
                Major.NONE,
                UserStatus.PENDING
        );
    }

    public User submitMajor(Major major) {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.totalToken,
                major,
                this.userStatus
        );
    }

    public User active() {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.totalToken,
                this.major,
                UserStatus.ACTIVE  // 상태를 ACTIVE로 변경
        );
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.userStatus);
    }

    public User updateSignupInfo(String username, String email, String name, String encodedPassword) {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                username,
                email,
                name,
                encodedPassword,
                this.role,
                this.profileImage,
                this.totalExp,
                this.totalCookie,
                this.totalToken,
                this.major,
                this.userStatus
        );
    }

    public User spendGoods(GoodsType goodsType, int amount) {
        int nextTotalCookie = this.totalCookie;
        int nextTotalToken = this.totalToken;

        switch (goodsType) {
            case COOKIE -> nextTotalCookie -= amount;
            case TOKEN -> nextTotalToken -= amount;
        }

        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                this.profileImage,
                this.totalExp,
                nextTotalCookie,
                nextTotalToken,
                this.major,
                this.userStatus
        );
    }

    public User updateProfileImage(String profileImage) {
        return new User(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                this.username,
                this.email,
                this.name,
                this.password,
                this.role,
                profileImage,
                this.totalExp,
                this.totalCookie,
                this.totalToken,
                this.major,
                this.userStatus
        );
    }
}
