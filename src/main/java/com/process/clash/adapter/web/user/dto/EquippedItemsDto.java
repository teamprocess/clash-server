package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.EquippedItemsData;

public class EquippedItemsDto {

    public record Response(
            Item insigma,
            Item nameplate,
            Item banner
    ) {
        public static Response from(EquippedItemsData data) {
            if (data == null) {
                return new Response(null, null, null);
            }
            return new Response(
                    Item.from(data.insigma()),
                    Item.from(data.nameplate()),
                    Item.from(data.banner())
            );
        }
    }

    public record Item(
            Long id,
            String name,
            String image
    ) {
        public static Item from(EquippedItemsData.EquippedItemData data) {
            if (data == null) {
                return null;
            }
            return new Item(data.id(), data.name(), data.image());
        }
    }
}
