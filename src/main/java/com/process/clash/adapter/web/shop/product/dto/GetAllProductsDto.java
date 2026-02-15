package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.common.exception.exception.ValidationException;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.data.GetAllProductsData;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetAllProductsDto {

    @Schema(name = "GetAllProductsDtoRequest")

    public record Request(
            Integer page,
            Integer size,
            ProductSortType sort,
            String category
    ) {
        public GetAllProductsData.Command toCommand(Actor actor) {
            ProductCategory productCategory = null;
            if (category != null && !category.equals("ALL")) {
                try {
                    productCategory = ProductCategory.valueOf(category.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ValidationException(e);
                }
            }
            return new GetAllProductsData.Command(actor, page, size, sort, productCategory);
        }
    }

    @Schema(name = "GetAllProductsDtoResponse")

    public record Response(
            List<CommonProductDto> products,
            Pagination pagination
    ) {
        public static Response from(GetAllProductsData.Result result) {
            List<CommonProductDto> products = result.products().stream()
                    .map(CommonProductDto::from)
                    .toList();
            return new Response(products, result.pagination());
        }
    }
}
