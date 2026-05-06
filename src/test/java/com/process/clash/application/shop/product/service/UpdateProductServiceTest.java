package com.process.clash.application.shop.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.shop.product.data.UpdateProductData;
import com.process.clash.application.shop.product.exception.exception.notfound.ProductNotFoundException;
import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.season.exception.exception.notfound.SeasonNotFoundException;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.season.entity.Season;
import com.process.clash.domain.user.user.enums.Role;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private SeasonRepositoryPort seasonRepositoryPort;

    @Mock
    private CheckAdminPolicy checkAdminPolicy;

    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        updateProductService = new UpdateProductService(
                productRepositoryPort,
                seasonRepositoryPort,
                checkAdminPolicy
        );
    }

    @Test
    @DisplayName("상품 정보를 수정한다")
    void execute_updatesProduct() {
        Actor actor = new Actor(1L, Role.ADMIN);
        Season currentSeason = new Season(2L, Instant.now(), Instant.now(), "2026 봄", LocalDate.now(), LocalDate.now().plusDays(30));
        Product product = Product.create(
                "기존 상품",
                ProductCategory.BANNER,
                "https://cdn.example.com/old.png",
                null,
                1000L,
                10,
                "기존 설명",
                null,
                true
        );
        product = new Product(10L, Instant.now(), Instant.now(), product.title(), product.category(), product.image(), null, product.price(), product.discount(), product.description(), 15L, null, false, true);

        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "변경 상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/new.png",
                null,
                2000L,
                20,
                "변경 설명",
                2L,
                null
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(seasonRepositoryPort.findById(2L)).thenReturn(Optional.of(currentSeason));
        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateProductData.Result result = updateProductService.execute(command);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(captor.capture());
        Product savedProduct = captor.getValue();

        assertThat(result.productId()).isEqualTo(10L);
        assertThat(savedProduct.title()).isEqualTo("변경 상품");
        assertThat(savedProduct.category()).isEqualTo(ProductCategory.NAMEPLATE);
        assertThat(savedProduct.image()).isEqualTo("https://cdn.example.com/new.png");
        assertThat(savedProduct.price()).isEqualTo(2000L);
        assertThat(savedProduct.discount()).isEqualTo(20);
        assertThat(savedProduct.description()).isEqualTo("변경 설명");
        assertThat(savedProduct.season()).isEqualTo(currentSeason);
        assertThat(savedProduct.isSeasonal()).isTrue();
        assertThat(savedProduct.isAblePurchase()).isTrue();
    }

    @Test
    @DisplayName("seasonId가 null이면 시즌 연결을 해제한다")
    void execute_clearsSeasonWhenSeasonIdIsNull() {
        Actor actor = new Actor(1L, Role.ADMIN);
        Season linkedSeason = new Season(3L, Instant.now(), Instant.now(), "2026 여름", LocalDate.now(), LocalDate.now().plusDays(30));
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/old.png",
                null,
                1000L,
                5,
                "설명",
                3L,
                linkedSeason,
                true,
                true
        );

        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/new.png",
                null,
                1000L,
                5,
                "설명",
                null,
                null
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        updateProductService.execute(command);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().season()).isNull();
        assertThat(captor.getValue().isSeasonal()).isFalse();
    }

    @Test
    @DisplayName("상품이 없으면 예외가 발생한다")
    void execute_throwsWhenProductNotFound() {
        Actor actor = new Actor(1L, Role.ADMIN);
        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/new.png",
                null,
                1000L,
                5,
                "설명",
                null,
                null
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProductService.execute(command))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("BGM 상품 수정 시 audio URL이 저장된다")
    void execute_savesBgmAudioUrl() {
        Actor actor = new Actor(1L, Role.ADMIN);
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "BGM 상품",
                ProductCategory.BGM,
                "https://cdn.example.com/cover.png",
                null,
                5000L,
                0,
                "배경음악",
                0L,
                null,
                false,
                true
        );

        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "BGM 상품",
                ProductCategory.BGM,
                "https://cdn.example.com/cover.png",
                "https://cdn.example.com/bgm.mp3",
                5000L,
                0,
                "배경음악",
                null,
                null
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        updateProductService.execute(command);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().category()).isEqualTo(ProductCategory.BGM);
        assertThat(captor.getValue().audio()).isEqualTo("https://cdn.example.com/bgm.mp3");
    }

    @Test
    @DisplayName("isAblePurchase를 false로 수정하면 저장된 상품에 반영된다")
    void execute_updatesIsAblePurchaseToFalse() {
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
                0,
                "설명",
                0L,
                null,
                false,
                true
        );

        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/product.png",
                null,
                1000L,
                0,
                "설명",
                null,
                false
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        updateProductService.execute(command);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().isAblePurchase()).isFalse();
    }

    @Test
    @DisplayName("isAblePurchase가 null이면 기존 값을 유지한다")
    void execute_preservesIsAblePurchaseWhenNull() {
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
                0,
                "설명",
                0L,
                null,
                false,
                false
        );

        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/product.png",
                null,
                1000L,
                0,
                "설명",
                null,
                null
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        updateProductService.execute(command);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().isAblePurchase()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 시즌 ID면 예외가 발생한다")
    void execute_throwsWhenSeasonNotFound() {
        Actor actor = new Actor(1L, Role.ADMIN);
        Product product = new Product(
                10L,
                Instant.now(),
                Instant.now(),
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/old.png",
                null,
                1000L,
                5,
                "설명",
                3L,
                null,
                false,
                true
        );
        UpdateProductData.Command command = new UpdateProductData.Command(
                actor,
                10L,
                "상품",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/new.png",
                null,
                1000L,
                5,
                "설명",
                999L,
                null
        );

        when(productRepositoryPort.findById(10L)).thenReturn(Optional.of(product));
        when(seasonRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProductService.execute(command))
                .isInstanceOf(SeasonNotFoundException.class);
    }
}
