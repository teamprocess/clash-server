package com.process.clash.application.profile.data;

public record EquippedItemsData(
        EquippedItemData insigma,
        EquippedItemData nameplate,
        EquippedItemData banner
) {
    public static EquippedItemsData empty() {
        return new EquippedItemsData(null, null, null);
    }

    public record EquippedItemData(
            Long id,
            String name,
            String image
    ) {
        public static EquippedItemData of(Long id, String name, String image) {
            return new EquippedItemData(id, name, image);
        }
    }
}
