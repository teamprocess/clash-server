package com.process.clash.application.profile.data;

import com.process.clash.application.common.actor.Actor;

public class EquipMyItemData {

    public record Command(
            Actor actor,
            Long productId
    ) {}

    public record Result(
            EquippedItemsData equippedItems
    ) {
        public static Result of(EquippedItemsData equippedItems) {
            return new Result(equippedItems);
        }
    }
}
