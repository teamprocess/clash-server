package com.process.clash.application.shop.product.data;

import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;

import java.util.ArrayList;
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
    ) {
        public static Result from(List<Product> domains, List<String> seasonTitles, Integer page, Integer size, Long totalItems) {

            // Product -> ProductVo 변환
            List<ProductVo> products = new ArrayList<>();
            for (int i = 0; i < domains.size(); i++) {
                String seasonTitle = seasonTitles.get(i);
                products.add(ProductVo.from(domains.get(i), seasonTitle));
            }

            PaginationInfo pagination = PaginationInfo.from(page, size, totalItems);

            return new Result(products, pagination);
        }
    }

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
