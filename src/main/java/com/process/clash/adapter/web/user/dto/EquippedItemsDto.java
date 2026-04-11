package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.EquippedItemsData;

public class EquippedItemsDto {

    public record Response(
            Item insignia,
            Item nameplate,
            Item banner,
            Item bgm
    ) {
        public static Response from(EquippedItemsData data) {
            if (data == null) {
                return new Response(null, null, null, null);
            }
            return new Response(
                    Item.from(data.insignia()),
                    Item.from(data.nameplate()),
                    Item.from(data.banner()),
                    Item.from(data.bgm())
            );
        }
    }

    public record Item(
            Long id,
            String name,
            String image,
            String audio
    ) {
        public static Item from(EquippedItemsData.EquippedItemData data) {
            if (data == null) {
                return null;
            }
            return new Item(data.id(), data.name(), data.image(), data.audio());
        }
    }
}
