package com.process.clash.application.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.data.UnequipMyItemData;
import com.process.clash.application.profile.exception.exception.forbidden.ItemNotOwnedException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.application.user.userequippeditem.port.out.UserEquippedItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnequipMyItemServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long PRODUCT_ID = 11L;

    @Mock private ProductRepositoryPort productRepositoryPort;
    @Mock private UserItemRepositoryPort userItemRepositoryPort;
    @Mock private UserEquippedItemRepositoryPort userEquippedItemRepositoryPort;
    @Mock private EquippedItemsAssembler equippedItemsAssembler;

    @InjectMocks
    private UnequipMyItemService service;

    @Test
    @DisplayName("장착 해제 시 대상 장착 정보를 삭제하고 최신 장착 목록을 반환한다")
    void unequip_deletesAndReturnsCurrentEquippedItems() {
        Product product = product(PRODUCT_ID, ProductCategory.BANNER);
        EquippedItemsData equippedItems = EquippedItemsData.empty();

        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(true);
        when(equippedItemsAssembler.loadByUserId(USER_ID)).thenReturn(equippedItems);

        UnequipMyItemData.Result result = service.execute(new UnequipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID));

        verify(userEquippedItemRepositoryPort).deleteByUserIdAndProductId(USER_ID, PRODUCT_ID);
        assertThat(result.equippedItems()).isEqualTo(equippedItems);
    }

    @Test
    @DisplayName("보유하지 않은 아이템은 장착 해제할 수 없다")
    void unequip_failsWhenItemNotOwned() {
        Product product = product(PRODUCT_ID, ProductCategory.NAMEPLATE);
        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.execute(new UnequipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID)))
                .isInstanceOf(ItemNotOwnedException.class);
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
