package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.EquipMyItemData;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.exception.exception.badrequest.InvalidEquippableItemCategoryException;
import com.process.clash.application.profile.exception.exception.badrequest.ItemNotOwnedException;
import com.process.clash.application.profile.port.in.EquipMyItemUsecase;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.application.user.userequippeditem.port.out.UserEquippedItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.user.userequippeditem.entity.UserEquippedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipMyItemService implements EquipMyItemUsecase {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;
    private final UserEquippedItemRepositoryPort userEquippedItemRepositoryPort;
    private final EquippedItemsAssembler equippedItemsAssembler;

    @Override
    public EquipMyItemData.Result execute(EquipMyItemData.Command command) {
        Long userId = command.actor().id();
        Product product = productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        if (!userItemRepositoryPort.existsByUserIdAndProductId(userId, product.id())) {
            throw new ItemNotOwnedException();
        }

        ProductCategory category = toEquippableCategory(product);

        userEquippedItemRepositoryPort.findByUserIdAndCategory(userId, category)
                .ifPresentOrElse(
                        userEquippedItem -> {
                            if (!userEquippedItem.productId().equals(product.id())) {
                                userEquippedItemRepositoryPort.save(userEquippedItem.changeProduct(product.id()));
                            }
                        },
                        () -> userEquippedItemRepositoryPort.save(UserEquippedItem.create(userId, product.id(), category))
                );

        EquippedItemsData equippedItems = equippedItemsAssembler.loadByUserId(userId);
        return EquipMyItemData.Result.of(equippedItems);
    }

    private ProductCategory toEquippableCategory(Product product) {
        ProductCategory category = product.category();
        if (category == ProductCategory.INSIGNIA
                || category == ProductCategory.NAMEPLATE
                || category == ProductCategory.BANNER) {
            return category;
        }
        throw new InvalidEquippableItemCategoryException();
    }
}
