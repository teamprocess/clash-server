package com.process.clash.adapter.web.shop.product.dto;

import com.process.clash.application.common.exception.exception.ValidationException;
import com.process.clash.application.shop.product.data.GetAllProductsData;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;
import java.util.List;

public class GetAllProductsDto {

    public record Request(
            Integer page,
            Integer size,
            ProductSortType sort,
            String category
    ) {
        public GetAllProductsData.Command toCommand() {
            ProductCategory productCategory = null;
            if (category != null && !category.equals("ALL")) {
                try {
                    productCategory = ProductCategory.valueOf(category.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ValidationException(e);
                }
            }
            return new GetAllProductsData.Command(page, size, sort, productCategory);
        }
    }

    public record Response(
            List<CommonProductDto> products,
            Pagination pagination
    ) {
        public static Response from(GetAllProductsData.Result result) {
            List<CommonProductDto> products = result.products().stream()
                    .map(CommonProductDto::from)
                    .toList();
            Pagination pagination = Pagination.from(result.pagination());
            return new Response(products, pagination);
        }
    }

    public record Pagination(
            Integer currentPage,
            Integer totalPages,
            Long totalItems,
            Integer pageSize,
            Boolean hasNext,
            Boolean hasPrevious
    ) {
        public static Pagination from(GetAllProductsData.PaginationInfo info) {
            return new Pagination(
                    info.currentPage(),
                    info.totalPages(),
                    info.totalItems(),
                    info.pageSize(),
                    info.hasNext(),
                    info.hasPrevious()
            );
        }
    }
}
