package com.process.clash.application.profile.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.UserItemCategory;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;
import java.time.LocalDate;
import java.util.List;

public class GetMyItemsData {

    public record Command(
        Actor actor,
        UserItemCategory category
    ) {}

    public record Result(
        List<Item> items
    ) {
        public static Result from(List<Item> items) {
            return new Result(items);
        }
    }

    public record Item(
        Long id,
        String title,
        String description,
        ProductCategory category,
        Long price,
        ProductGoodsType priceType,
        Integer discount,
        Long popularity,
        Boolean isSeasonal,
        Season season
    ) {
        public static Item of(
            Long id,
            String title,
            String description,
            ProductCategory category,
            Long price,
            ProductGoodsType priceType,
            Integer discount,
            Long popularity,
            Boolean isSeasonal,
            Season season
        ) {
            return new Item(
                id,
                title,
                description,
                category,
                price,
                priceType,
                discount,
                popularity,
                isSeasonal,
                season
            );
        }
    }

    public record Season(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
    ) {
        public static Season of(Long id, String name, LocalDate startDate, LocalDate endDate) {
            return new Season(id, name, startDate, endDate);
        }
    }
}
