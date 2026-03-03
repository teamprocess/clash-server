package com.process.clash.application.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.GetMyItemsData;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.domain.common.enums.UserItemCategory;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMyItemsServiceTest {

    private static final Long USER_ID = 1L;

    @Mock private UserItemRepositoryPort userItemRepositoryPort;
    @Mock private ProductRepositoryPort productRepositoryPort;

    private GetMyItemsService service;

    @BeforeEach
    void setUp() {
        service = new GetMyItemsService(userItemRepositoryPort, productRepositoryPort);
    }

    @Test
    @DisplayName("보유 아이템 조회는 user_items 기준 product id 목록으로 조회한다")
    void execute_loadsOwnedProductsFromUserItems() {
        when(userItemRepositoryPort.findProductIdsByUserId(USER_ID)).thenReturn(List.of(3L, 1L));
        when(productRepositoryPort.findAllByIdIn(List.of(3L, 1L))).thenReturn(List.of(
                product(3L, ProductCategory.BANNER),
                product(1L, ProductCategory.NAMEPLATE)
        ));

        GetMyItemsData.Result result = service.execute(new GetMyItemsData.Command(new Actor(USER_ID), UserItemCategory.ALL));

        verify(userItemRepositoryPort).findProductIdsByUserId(USER_ID);
        assertThat(result.items()).hasSize(2);
        assertThat(result.items().get(0).id()).isEqualTo(1L);
        assertThat(result.items().get(1).id()).isEqualTo(3L);
    }

    @Test
    @DisplayName("INSIGMA 타입 요청은 INSIGNIA 카테고리 아이템만 필터링한다")
    void execute_filtersInsigmaAliasToInsignia() {
        when(userItemRepositoryPort.findProductIdsByUserId(USER_ID)).thenReturn(List.of(1L, 2L));
        when(productRepositoryPort.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(
                product(1L, ProductCategory.INSIGNIA),
                product(2L, ProductCategory.BANNER)
        ));

        GetMyItemsData.Result result = service.execute(new GetMyItemsData.Command(new Actor(USER_ID), UserItemCategory.INSIGMA));

        assertThat(result.items()).hasSize(1);
        assertThat(result.items().getFirst().category()).isEqualTo(ProductCategory.INSIGNIA);
    }

    private Product product(Long id, ProductCategory category) {
        return new Product(
                id,
                Instant.now().minusSeconds(300),
                Instant.now().minusSeconds(100),
                "item-" + id,
                category,
                "https://cdn.example.com/item.png",
                1000L,
                10,
                "desc",
                0L,
                null,
                false
        );
    }
}
