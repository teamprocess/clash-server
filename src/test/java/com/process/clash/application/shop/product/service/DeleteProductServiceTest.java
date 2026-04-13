package com.process.clash.application.shop.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.shop.product.data.DeleteProductData;
import com.process.clash.application.shop.product.exception.exception.conflict.ProductInUseException;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.user.user.enums.Role;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class DeleteProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private CheckAdminPolicy checkAdminPolicy;

    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        deleteProductService = new DeleteProductService(productRepositoryPort, checkAdminPolicy);
    }

    @Test
    @DisplayName("상품을 삭제한다")
    void execute_deletesProduct() {
        Actor actor = new Actor(1L, Role.ADMIN);
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/product.png",
                null,
                1000L,
                5,
                "설명",
                0L,
                null,
                false
        );
        DeleteProductData.Command command = new DeleteProductData.Command(actor, 10L);

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));

        DeleteProductData.Result result = deleteProductService.execute(command);

        assertThat(result.productId()).isEqualTo(10L);
        verify(productRepositoryPort).deleteById(10L);
    }

    @Test
    @DisplayName("상품이 없으면 예외가 발생한다")
    void execute_throwsWhenProductNotFound() {
        Actor actor = new Actor(1L, Role.ADMIN);
        DeleteProductData.Command command = new DeleteProductData.Command(actor, 10L);

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteProductService.execute(command))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("삭제 시 FK 제약이 있으면 충돌 예외로 변환한다")
    void execute_throwsConflictWhenDeleteFailsByIntegrity() {
        Actor actor = new Actor(1L, Role.ADMIN);
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/product.png",
                null,
                1000L,
                5,
                "설명",
                0L,
                null,
                false
        );
        DeleteProductData.Command command = new DeleteProductData.Command(actor, 10L);

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        doThrow(new DataIntegrityViolationException("fk constraint"))
                .when(productRepositoryPort).deleteById(10L);

        assertThatThrownBy(() -> deleteProductService.execute(command))
                .isInstanceOf(ProductInUseException.class);
    }
}
