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
            List<Product> products,
            Pagination pagination
    ) {
        public static Response from(GetAllProductsData.Result result) {
            List<Product> products = result.products().stream()
                    .map(Product::from)
                    .toList();
            Pagination pagination = Pagination.from(result.pagination());
            return new Response(products, pagination);
        }
    }

    public record Product(
            Long id,
            String title,
            String category,
            String image,
            String type,
            Long price,
            Integer discount,
            String description,
            Long popularity,
            String seasonName,
            Boolean isSeasonal,
            String createdAt
    ) {
        public static Product from(ProductVo product) {
            return new Product(
                    product.id(),
                    product.title(),
                    product.category().name(),
                    product.image(),
                    product.type().name(),
                    product.price(),
                    product.discount(),
                    product.description(),
                    product.popularity(),
                    product.seasonName(),
                    product.isSeasonal(),
                    product.createdAt().toString()
            );
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
