package com.process.clash.domain.user.userrankhistory.enums;

public enum RankTier {
    NONE,
    MASTER,
    AURA;

    public static RankTier fromString(String value) {
        if (value == null) return NONE;
        try {
            return RankTier.valueOf(value);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}
