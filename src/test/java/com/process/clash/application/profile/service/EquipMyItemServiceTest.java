package com.process.clash.application.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.profile.data.EquipMyItemData;
import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.application.profile.exception.exception.badrequest.InvalidEquippableItemCategoryException;
import com.process.clash.application.profile.exception.exception.forbidden.ItemNotOwnedException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.user.useritem.port.out.UserItemRepositoryPort;
import com.process.clash.application.user.userequippeditem.port.out.UserEquippedItemRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.user.userequippeditem.entity.UserEquippedItem;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class EquipMyItemServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long PRODUCT_ID = 11L;

    @Mock private ProductRepositoryPort productRepositoryPort;
    @Mock private UserItemRepositoryPort userItemRepositoryPort;
    @Mock private UserEquippedItemRepositoryPort userEquippedItemRepositoryPort;
    @Mock private EquippedItemsAssembler equippedItemsAssembler;

    @InjectMocks
    private EquipMyItemService service;

    @Test
    @DisplayName("장착 시 같은 카테고리 슬롯이 없으면 새 장착 데이터를 저장한다")
    void equip_createsNewSlotWhenMissing() {
        Product product = product(PRODUCT_ID, ProductCategory.INSIGNIA);
        EquippedItemsData equippedItems = EquippedItemsData.empty();

        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(true);
        when(userEquippedItemRepositoryPort.findByUserIdAndCategory(USER_ID, ProductCategory.INSIGNIA))
                .thenReturn(Optional.empty());
        when(equippedItemsAssembler.loadByUserId(USER_ID)).thenReturn(equippedItems);

        EquipMyItemData.Result result = service.execute(new EquipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID));

        verify(userEquippedItemRepositoryPort).save(UserEquippedItem.create(USER_ID, PRODUCT_ID, ProductCategory.INSIGNIA));
        assertThat(result.equippedItems()).isEqualTo(equippedItems);
    }

    @Test
    @DisplayName("이미 같은 아이템이 장착되어 있으면 저장하지 않고 현재 장착 상태만 반환한다")
    void equip_skipSaveWhenAlreadyEquippedSameProduct() {
        Product product = product(PRODUCT_ID, ProductCategory.NAMEPLATE);
        UserEquippedItem equippedItem = new UserEquippedItem(
                3L,
                Instant.now().minusSeconds(100),
                Instant.now().minusSeconds(50),
                USER_ID,
                PRODUCT_ID,
                ProductCategory.NAMEPLATE
        );

        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(true);
        when(userEquippedItemRepositoryPort.findByUserIdAndCategory(USER_ID, ProductCategory.NAMEPLATE))
                .thenReturn(Optional.of(equippedItem));
        when(equippedItemsAssembler.loadByUserId(USER_ID)).thenReturn(EquippedItemsData.empty());

        service.execute(new EquipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID));

        verify(userEquippedItemRepositoryPort, never()).save(equippedItem.changeProduct(PRODUCT_ID));
    }

    @Test
    @DisplayName("동시에 같은 카테고리 장착 요청이 들어와 unique 충돌이 발생하면 재조회 후 슬롯을 갱신한다")
    void equip_recoversFromConcurrentUniqueConflict() {
        Product product = product(PRODUCT_ID, ProductCategory.INSIGNIA);
        UserEquippedItem concurrentEquippedItem = new UserEquippedItem(
                7L,
                Instant.now().minusSeconds(90),
                Instant.now().minusSeconds(40),
                USER_ID,
                99L,
                ProductCategory.INSIGNIA
        );
        EquippedItemsData equippedItems = EquippedItemsData.empty();

        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(true);
        when(userEquippedItemRepositoryPort.findByUserIdAndCategory(USER_ID, ProductCategory.INSIGNIA))
                .thenReturn(Optional.empty(), Optional.of(concurrentEquippedItem));
        when(userEquippedItemRepositoryPort.save(UserEquippedItem.create(USER_ID, PRODUCT_ID, ProductCategory.INSIGNIA)))
                .thenThrow(new DataIntegrityViolationException("duplicate key"));
        when(equippedItemsAssembler.loadByUserId(USER_ID)).thenReturn(equippedItems);

        EquipMyItemData.Result result = service.execute(new EquipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID));

        verify(userEquippedItemRepositoryPort).save(concurrentEquippedItem.changeProduct(PRODUCT_ID));
        assertThat(result.equippedItems()).isEqualTo(equippedItems);
    }

    @Test
    @DisplayName("보유하지 않은 아이템은 장착할 수 없다")
    void equip_failsWhenItemNotOwned() {
        Product product = product(PRODUCT_ID, ProductCategory.BANNER);
        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.execute(new EquipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID)))
                .isInstanceOf(ItemNotOwnedException.class);
    }

    @Test
    @DisplayName("장착 불가능 카테고리 상품은 예외를 던진다")
    void equip_failsWhenCategoryIsNotEquippable() {
        Product product = product(PRODUCT_ID, null);
        when(productRepositoryPort.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(userItemRepositoryPort.existsByUserIdAndProductId(USER_ID, PRODUCT_ID)).thenReturn(true);

        assertThatThrownBy(() -> service.execute(new EquipMyItemData.Command(new Actor(USER_ID), PRODUCT_ID)))
                .isInstanceOf(InvalidEquippableItemCategoryException.class);
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
