package com.process.clash.application.shop.product.port.in;

import com.process.clash.application.shop.product.data.SearchProductsData;

public interface SearchProductsUseCase {
    SearchProductsData.Result execute(SearchProductsData.Command command);
}
