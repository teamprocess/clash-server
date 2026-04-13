package com.process.clash.application.profile.data;

public record EquippedItemsData(
        EquippedItemData insignia,
        EquippedItemData nameplate,
        EquippedItemData banner,
        EquippedItemData bgm
) {
    public static EquippedItemsData empty() {
        return new EquippedItemsData(null, null, null, null);
    }

    public record EquippedItemData(
            Long id,
            String name,
            String image,
            String audio
    ) {
        public static EquippedItemData of(Long id, String name, String image, String audio) {
            return new EquippedItemData(id, name, image, audio);
        }
    }
}
