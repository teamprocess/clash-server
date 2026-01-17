package com.process.clash.application.shop.product.data;

import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;

import java.util.List;

public class GetAllProductsData {

    public record Command(
            Integer page,
            Integer size,
            ProductSortType sort,
            ProductCategory category
    ) {
        public Command {
            if (page == null || page < 1) {
                page = 1;
            }
            if (size == null || size < 1) {
                size = 40;
            }
            if (sort == null) {
                sort = ProductSortType.LATEST;
            }
        }
    }

    public record Result(
            List<ProductVo> products,
            PaginationInfo pagination
    ) {}

    public record PaginationInfo(
            Integer currentPage,
            Integer totalPages,
            Long totalItems,
            Integer pageSize,
            Boolean hasNext,
            Boolean hasPrevious
    ) {
        public static PaginationInfo from(Integer currentPage, Integer pageSize, Long totalItems) {
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            boolean hasNext = currentPage < totalPages;
            boolean hasPrevious = currentPage > 1;

            return new PaginationInfo(
                    currentPage,
                    totalPages,
                    totalItems,
                    pageSize,
                    hasNext,
                    hasPrevious
            );
        }
    }
}
