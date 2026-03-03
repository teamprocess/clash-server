package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.GetMyItemsData;
import com.process.clash.application.profile.port.in.GetMyItemsUsecase;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.common.enums.UserItemCategory;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.season.entity.Season;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyItemsService implements GetMyItemsUsecase {

    private final UserItemRepositoryPort userItemRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public GetMyItemsData.Result execute(GetMyItemsData.Command command) {
        List<Long> productIds = userItemRepositoryPort.findProductIdsByUserId(command.actor().id());

        if (productIds.isEmpty()) {
            return GetMyItemsData.Result.from(List.of());
        }

        List<Product> products = productRepositoryPort.findAllByIdIn(productIds)
                .stream()
                .filter(product -> matchesCategory(command.category(), product.category()))
                .sorted(Comparator.comparing(Product::id))
                .toList();

        List<GetMyItemsData.Item> items = products.stream()
                .map(this::toItem)
                .toList();

        return GetMyItemsData.Result.from(items);
    }

    private boolean matchesCategory(UserItemCategory category, ProductCategory productCategory) {
        if (category == null || category == UserItemCategory.ALL) {
            return true;
        }
        if (productCategory == null) {
            return false;
        }
        return switch (category) {
            case INSIGMA, INSIGNIA -> productCategory == ProductCategory.INSIGNIA;
            case NAMEPLATE -> productCategory == ProductCategory.NAMEPLATE;
            case BANNER -> productCategory == ProductCategory.BANNER;
            default -> true;
        };
    }

    private GetMyItemsData.Item toItem(Product product) {
        GetMyItemsData.Season season = toSeason(product.isSeasonal(), product.season());

        return GetMyItemsData.Item.of(
                product.id(),
                product.title(),
                product.image(),
                product.description(),
                product.category(),
                product.price(),
                product.discount(),
                product.popularity(),
                product.isSeasonal(),
                season
        );
    }

    private GetMyItemsData.Season toSeason(Boolean isSeasonal, Season season) {
        if (isSeasonal == null || !isSeasonal || season == null) {
            return null;
        }
        return GetMyItemsData.Season.of(
                season.id(),
                season.name(),
                season.startDate(),
                season.endDate()
        );
    }
}
