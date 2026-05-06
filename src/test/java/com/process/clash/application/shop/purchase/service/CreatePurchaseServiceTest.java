package com.process.clash.application.shop.purchase.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.exception.exception.badrequest.NotAblePurchaseException;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.purchase.data.CreatePurchaseData;
import com.process.clash.application.shop.purchase.exception.exception.conflict.AlreadyOwnedProductException;
import com.process.clash.application.shop.purchase.port.out.PurchaseRepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.usergoodshistory.port.out.UserGoodsHistoryRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatePurchaseServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private PurchaseRepositoryPort purchaseRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserGoodsHistoryRepositoryPort userGoodsHistoryRepositoryPort;

    @Mock
    private UserItemRepositoryPort userItemRepositoryPort;

    private CreatePurchaseService createPurchaseService;

    @BeforeEach
    void setUp() {
        createPurchaseService = new CreatePurchaseService(
                productRepositoryPort,
                purchaseRepositoryPort,
                userRepositoryPort,
                userGoodsHistoryRepositoryPort,
                userItemRepositoryPort
        );
    }

    @Test
    @DisplayName("상품을 찾지 못하면 예외가 발생한다")
    void execute_throwsWhenProductNotFound() {
        Actor actor = new Actor(1L);
        CreatePurchaseData.Command command = new CreatePurchaseData.Command(actor, 99L);

        when(productRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createPurchaseService.execute(command))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("isAblePurchase가 false인 상품은 구매할 수 없다")
    void execute_throwsWhenProductNotAblePurchase() {
        Actor actor = new Actor(1L);
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "구매 불가 상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/product.png",
                null,
                1000L,
                0,
                "설명",
                0L,
                null,
                false,
                false
        );
        CreatePurchaseData.Command command = new CreatePurchaseData.Command(actor, 10L);

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> createPurchaseService.execute(command))
                .isInstanceOf(NotAblePurchaseException.class);
    }

    @Test
    @DisplayName("이미 보유한 상품은 구매할 수 없다")
    void execute_throwsWhenAlreadyOwned() {
        Actor actor = new Actor(1L);
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/product.png",
                null,
                1000L,
                0,
                "설명",
                0L,
                null,
                false,
                true
        );
        CreatePurchaseData.Command command = new CreatePurchaseData.Command(actor, 10L);

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> createPurchaseService.execute(command))
                .isInstanceOf(AlreadyOwnedProductException.class);
    }
}