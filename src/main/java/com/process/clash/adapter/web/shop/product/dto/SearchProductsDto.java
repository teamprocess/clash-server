package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.exception.exception.ValidationException;
import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.shop.product.data.SearchProductsData;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class SearchProductsDto {

    @Schema(name = "SearchProductsDtoRequest")
    public record Request(
            String keyword,
            Integer page,
            Integer size,
            ProductSortType sort,
            String category
    ) {
        public SearchProductsData.Command toCommand(Actor actor) {
            ProductCategory productCategory = null;
            if (category != null && !category.equals("ALL")) {
                try {
                    productCategory = ProductCategory.valueOf(category.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ValidationException(e);
                }
            }
            return new SearchProductsData.Command(actor, keyword, page, size, sort, productCategory);
        }
    }

    @Schema(name = "SearchProductsDtoResponse")
    public record Response(
            List<CommonProductDto> products,
            Pagination pagination
    ) {
        public static Response from(SearchProductsData.Result result) {
            List<CommonProductDto> products = result.products().stream()
                    .map(CommonProductDto::from)
                    .toList();
            return new Response(products, result.pagination());
        }
    }
}
