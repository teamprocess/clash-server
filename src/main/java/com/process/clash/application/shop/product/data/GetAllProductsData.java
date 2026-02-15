package com.process.clash.application.shop.product.data;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.vo.ProductVo;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;
import java.util.List;

public class GetAllProductsData {

    public record Command(
            Actor actor,
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
            Pagination pagination
    ) {}
}
