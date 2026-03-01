package com.process.clash.adapter.web.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.Instant;

public class GetMyProfileDto {

    public record Response(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String name,
        String email,
        Role role,
        String profileImage,
        int totalExp,
        int totalCookie,
        int totalToken,
        Major major,
        UserStatus userStatus,
        boolean githubLinked,
        UserActivityStatus activityStatus,
        @JsonProperty("equipped_items")
        EquippedItems equippedItems
    ) {
        public static GetMyProfileDto.Response from(GetMyProfileData.Result result) {
            return new Response(
              result.id(),
              result.createdAt(),
              result.updatedAt(),
              result.username(),
              result.name(),
              result.email(),
              result.role(),
              result.profileImage(),
              result.totalExp(),
              result.totalCookie(),
              result.totalToken(),
              result.major(),
              result.userStatus(),
              result.githubLinked(),
              result.activityStatus(),
              EquippedItems.from(result.equippedItems())
            );
        }
    }

    public record EquippedItems(
            Item insigma,
            Item nameplate,
            Item banner
    ) {
        public static EquippedItems from(EquippedItemsData data) {
            if (data == null) {
                return null;
            }
            return new EquippedItems(
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
