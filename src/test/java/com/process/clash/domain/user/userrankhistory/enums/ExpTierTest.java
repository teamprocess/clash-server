package com.process.clash.domain.user.userrankhistory.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExpTierTest {

    @Test
    @DisplayName("exp가 50000 이상이면 DIAMOND다")
    void fromExp_overOrEqual50k_returnsDiamond() {
        assertThat(ExpTier.fromExp(50_000)).isEqualTo(ExpTier.DIAMOND);
    }

    @Test
    @DisplayName("exp가 49999 이하이면 GOLD다")
    void fromExp_under50k_returnsGold() {
        assertThat(ExpTier.fromExp(49_999)).isEqualTo(ExpTier.GOLD);
    }
}
