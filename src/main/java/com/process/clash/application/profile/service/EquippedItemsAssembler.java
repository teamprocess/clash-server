package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.userequippeditem.port.out.UserEquippedItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.user.userequippeditem.entity.UserEquippedItem;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EquippedItemsAssembler {

    private final UserEquippedItemRepositoryPort userEquippedItemRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public EquippedItemsData loadByUserId(Long userId) {
        return loadByUserIds(List.of(userId)).getOrDefault(userId, EquippedItemsData.empty());
    }

    public Map<Long, EquippedItemsData> loadByUserIds(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, EquippedItemsData> result = userIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        ignored -> EquippedItemsData.empty(),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        List<UserEquippedItem> equippedItems = userEquippedItemRepositoryPort.findAllByUserIds(userIds);
        if (equippedItems.isEmpty()) {
            return result;
        }

        List<Long> productIds = equippedItems.stream()
                .map(UserEquippedItem::productId)
                .distinct()
                .toList();
        Map<Long, Product> productById = productRepositoryPort.findAllByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::id, Function.identity()));

        Map<Long, List<UserEquippedItem>> equippedByUserId = equippedItems.stream()
                .collect(Collectors.groupingBy(UserEquippedItem::userId));

        equippedByUserId.forEach((userId, userEquippedItems) ->
                result.put(userId, toEquippedItems(userEquippedItems, productById))
        );

        return result;
    }

    private EquippedItemsData toEquippedItems(
            List<UserEquippedItem> userEquippedItems,
            Map<Long, Product> productById
    ) {
        EquippedItemsData.EquippedItemData insigma = null;
        EquippedItemsData.EquippedItemData nameplate = null;
        EquippedItemsData.EquippedItemData banner = null;

        for (UserEquippedItem userEquippedItem : userEquippedItems) {
            Product product = productById.get(userEquippedItem.productId());
            if (product == null) {
                continue;
            }
            if (product.category() != userEquippedItem.category()) {
                continue;
            }

            EquippedItemsData.EquippedItemData summary = EquippedItemsData.EquippedItemData.of(
                    product.id(),
                    product.title(),
                    product.image()
            );

            if (userEquippedItem.category() == ProductCategory.INSIGNIA) {
                insigma = summary;
            } else if (userEquippedItem.category() == ProductCategory.NAMEPLATE) {
                nameplate = summary;
            } else if (userEquippedItem.category() == ProductCategory.BANNER) {
                banner = summary;
            }
        }

        return new EquippedItemsData(insigma, nameplate, banner);
    }
}
