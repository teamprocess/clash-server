package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetMyItemsData;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.LocalDate;
import java.util.List;

public class GetMyItemsDto {

    public record Response(
        List<Item> items
    ) {
        public static Response from(GetMyItemsData.Result result) {
            List<Item> items = result.items().stream()
                    .map(Item::from)
                    .toList();
            return new Response(items);
        }
    }

    public record Item(
        Long id,
        String title,
        String description,
        ProductCategory category,
        Long price,
        Integer discount,
        Long popularity,
        Boolean isSeasonal,
        Season season
    ) {
        public static Item from(GetMyItemsData.Item item) {
            return new Item(
                    item.id(),
                    item.title(),
                    item.description(),
                    item.category(),
                    item.price(),
                    item.discount(),
                    item.popularity(),
                    item.isSeasonal(),
                    Season.from(item.season())
            );
        }
    }

    public record Season(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
    ) {
        public static Season from(GetMyItemsData.Season season) {
            if (season == null) {
                return null;
            }
            return new Season(
                    season.id(),
                    season.name(),
                    season.startDate(),
                    season.endDate()
            );
        }
    }
}
