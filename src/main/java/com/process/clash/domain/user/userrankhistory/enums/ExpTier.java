package com.process.clash.domain.user.userrankhistory.enums;

public enum ExpTier {
    UNRANKED,   // 0 ~ 999
    BRONZE,     // 1,000 ~ 9,999
    SILVER,     // 10,000 ~ 29,999
    GOLD,       // 30,000 ~ 49,999
    DIAMOND;    // 50,000+

    public static ExpTier fromExp(int exp) {
        if (exp >= 50_000) return DIAMOND;
        if (exp >= 30_000) return GOLD;
        if (exp >= 10_000) return SILVER;
        if (exp >= 1_000)  return BRONZE;
        return UNRANKED;
    }

    public static ExpTier fromString(String value) {
        if (value == null) return UNRANKED;
        try {
            return ExpTier.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNRANKED;
        }
    }
}
