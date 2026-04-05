package com.process.clash.application.shop.recommendedproduct.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.application.shop.product.service.ProductVoConverter;
import com.process.clash.application.shop.recommendedproduct.data.GetRecommendedProductsData;
import com.process.clash.application.shop.recommendedproduct.port.out.RecommendedProductRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.recommendedproduct.entity.RecommendedProduct;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRecommendedProductsServiceTest {

    @Mock
    private RecommendedProductRepositoryPort recommendedProductRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private ProductVoConverter productVoConverter;

    private GetRecommendedProductsService getRecommendedProductsService;

    @BeforeEach
    void setUp() {
        getRecommendedProductsService = new GetRecommendedProductsService(
                recommendedProductRepositoryPort,
                productRepositoryPort,
                productVoConverter
        );
    }

    @Test
    @DisplayName("추천 상품 조회는 오늘 날짜 기준 활성 추천만 조회한다")
    void execute_loadsRecommendationsForToday() {
        when(recommendedProductRepositoryPort.findTop10ActiveByDateOrderByDisplayOrder(any()))
                .thenReturn(List.of());
        when(productRepositoryPort.findAllByIdIn(List.of())).thenReturn(List.of());
        when(productVoConverter.toProductVos(List.of(), null)).thenReturn(List.of());

        getRecommendedProductsService.execute(new GetRecommendedProductsData.Command(null));

        ArgumentCaptor<LocalDate> captor = ArgumentCaptor.forClass(LocalDate.class);
        verify(recommendedProductRepositoryPort).findTop10ActiveByDateOrderByDisplayOrder(captor.capture());
        assertThat(captor.getValue()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("추천 상품 응답은 추천 displayOrder 순서를 유지한다")
    void execute_preservesRecommendationOrder() {
        RecommendedProduct second = new RecommendedProduct(
                2L,
                Instant.now(),
                Instant.now(),
                200L,
                1,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                true
        );
        RecommendedProduct first = new RecommendedProduct(
                1L,
                Instant.now(),
                Instant.now(),
                100L,
                2,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                true
        );
        Product product100 = new Product(
                100L,
                Instant.now(),
                Instant.now(),
                "상품100",
                ProductCategory.BANNER,
                "https://cdn.example.com/100.png",
                1000L,
                0,
                "상품100 설명",
                0L,
                null,
                false
        );
        Product product200 = new Product(
                200L,
                Instant.now(),
                Instant.now(),
                "상품200",
                ProductCategory.NAMEPLATE,
                "https://cdn.example.com/200.png",
                2000L,
                0,
                "상품200 설명",
                0L,
                null,
                false
        );

        when(recommendedProductRepositoryPort.findTop10ActiveByDateOrderByDisplayOrder(any()))
                .thenReturn(List.of(second, first));
        when(productRepositoryPort.findAllByIdIn(List.of(200L, 100L)))
                .thenReturn(List.of(product100, product200));
        when(productVoConverter.toProductVos(any(), any())).thenReturn(List.of());

        getRecommendedProductsService.execute(new GetRecommendedProductsData.Command(null));

        ArgumentCaptor<List<Product>> captor = ArgumentCaptor.forClass(List.class);
        verify(productVoConverter).toProductVos(captor.capture(), any());
        assertThat(captor.getValue())
                .extracting(Product::id)
                .containsExactly(200L, 100L);
    }
}
