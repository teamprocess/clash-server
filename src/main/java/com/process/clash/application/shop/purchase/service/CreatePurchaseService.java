package com.process.clash.application.shop.purchase.service;

import com.process.clash.application.shop.purchase.data.CreatePurchaseData;
import com.process.clash.application.shop.purchase.exception.exception.badrequest.InsufficientGoodsException;
import com.process.clash.application.shop.purchase.exception.exception.conflict.AlreadyOwnedProductException;
import com.process.clash.application.shop.purchase.exception.exception.badrequest.PriceTooLargeException;
import com.process.clash.application.shop.purchase.port.in.CreatePurchaseUseCase;
import com.process.clash.application.shop.purchase.port.out.PurchaseRepositoryPort;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.common.enums.GoodsActingCategory;
import com.process.clash.domain.common.enums.GoodsType;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.purchase.entity.Purchase;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import com.process.clash.domain.user.useritem.entity.UserItem;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatePurchaseService implements CreatePurchaseUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final PurchaseRepositoryPort purchaseRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;
    private final UserItemRepositoryPort userItemRepositoryPort;

    @Override
    public CreatePurchaseData.Result execute(CreatePurchaseData.Command command) {
        Product product = productRepositoryPort.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        Long userId = command.actor().id();

        if (userItemRepositoryPort.existsByUserIdAndProductId(userId, product.id())) {
            throw new AlreadyOwnedProductException();
        }

        User user = userRepositoryPort.findByIdForUpdate(userId)
                .orElseThrow(UserNotFoundException::new);

        long price = calculatePrice(product);
        int amount = toIntPrice(price);
        GoodsType goodsType = product.type().toGoodsType();

        if (!hasEnoughGoods(user, goodsType, amount)) {
            throw new InsufficientGoodsException();
        }

        User updatedUser = user.spendGoods(goodsType, amount);
        userRepositoryPort.save(updatedUser);

        productRepositoryPort.save(product.increasePopularity());

        Purchase savedPurchase = purchaseRepositoryPort.save(
                Purchase.create(goodsType, price, product.id(), userId)
        );

        try {
            userItemRepositoryPort.save(UserItem.create(userId, product.id()));
        } catch (DataIntegrityViolationException exception) {
            throw new AlreadyOwnedProductException(exception);
        }

        userGoodsHistoryRepositoryPort.save(new UserGoodsHistory(
                null,
                null,
                goodsType,
                GoodsActingCategory.PURCHASE,
                -amount,
                product.id(),
                userId
        ));

        return CreatePurchaseData.Result.from(savedPurchase.id());
    }

    private long calculatePrice(Product product) {
        int discount = product.discount() != null ? product.discount() : 0;
        return product.price() * (100 - discount) / 100;
    }

    private int toIntPrice(long price) {
        if (price > Integer.MAX_VALUE) {
            throw new PriceTooLargeException();
        }
        return (int) price;
    }

    private boolean hasEnoughGoods(User user, GoodsType goodsType, int amount) {
        return switch (goodsType) {
            case COOKIE -> user.totalCookie() >= amount;
            case TOKEN -> user.totalToken() >= amount;
        };
    }
}
