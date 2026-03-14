package com.process.clash.domain.user.userrankhistory.enums;

import com.process.clash.domain.user.userrankhistory.enums.ExpTier;

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

    public static String resolveTier(RankTier rankTier, ExpTier expTier) {
        return rankTier == NONE ? expTier.name() : rankTier.name();
    }
}
