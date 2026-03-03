package com.process.clash.application.profile.service;

import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.data.UnequipMyItemData;
import com.process.clash.application.profile.port.in.UnequipMyItemUsecase;
import com.process.clash.application.profile.exception.exception.forbidden.ItemNotOwnedException;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.application.user.userequippeditem.port.out.UserEquippedItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnequipMyItemService implements UnequipMyItemUsecase {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;
    private final UserEquippedItemRepositoryPort userEquippedItemRepositoryPort;
    private final EquippedItemsAssembler equippedItemsAssembler;

    @Override
    public UnequipMyItemData.Result execute(UnequipMyItemData.Command command) {
        Long userId = command.actor().id();
        Product product = productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        if (!userItemRepositoryPort.existsByUserIdAndProductId(userId, product.id())) {
            throw new ItemNotOwnedException();
        }

        userEquippedItemRepositoryPort.deleteByUserIdAndProductId(userId, product.id());

        EquippedItemsData equippedItems = equippedItemsAssembler.loadByUserId(userId);
        return UnequipMyItemData.Result.of(equippedItems);
    }
}
