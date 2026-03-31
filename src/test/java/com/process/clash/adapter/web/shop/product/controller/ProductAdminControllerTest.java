package com.process.clash.adapter.web.shop.product.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.product.data.CreateProductData;
import com.process.clash.application.shop.product.data.DeleteProductData;
import com.process.clash.application.shop.product.data.UpdateProductData;
import com.process.clash.application.shop.product.port.in.CreateProductUseCase;
import com.process.clash.application.shop.product.port.in.DeleteProductUseCase;
import com.process.clash.application.shop.product.port.in.UpdateProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class ProductAdminControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private UpdateProductUseCase updateProductUseCase;

    @Mock
    private DeleteProductUseCase deleteProductUseCase;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        ProductAdminController controller = new ProductAdminController(
                createProductUseCase,
                updateProductUseCase,
                deleteProductUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(authenticatedActorResolver())
                .build();
    }

    @Test
    @DisplayName("POST /api/admin/shop/products 는 상품 생성 요청을 처리한다")
    void createProduct_returnsCreatedResponse() throws Exception {
        when(createProductUseCase.execute(any()))
                .thenReturn(new CreateProductData.Result(100L));

        mockMvc.perform(post("/api/admin/shop/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "신규 상품",
                                  "category": "NAMEPLATE",
                                  "image": "https://cdn.example.com/products/100.png",
                                  "price": 12000,
                                  "discount": 10,
                                  "description": "설명",
                                  "seasonId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("상품 생성에 성공했습니다."))
                .andExpect(jsonPath("$.data.productId").value(100));

        ArgumentCaptor<CreateProductData.Command> captor = ArgumentCaptor.forClass(CreateProductData.Command.class);
        verify(createProductUseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().seasonId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("PUT /api/admin/shop/products/{productId} 는 상품 수정 요청을 처리한다")
    void updateProduct_returnsSuccessResponse() throws Exception {
        when(updateProductUseCase.execute(any()))
                .thenReturn(new UpdateProductData.Result(100L, java.time.Instant.now()));

        mockMvc.perform(put("/api/admin/shop/products/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of(
                                "title", "수정 상품",
                                "category", "BANNER",
                                "image", "https://cdn.example.com/products/100-v2.png",
                                "price", 15000,
                                "discount", 15,
                                "description", "수정 설명"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 수정에 성공했습니다."))
                .andExpect(jsonPath("$.data.productId").value(100));

        ArgumentCaptor<UpdateProductData.Command> captor = ArgumentCaptor.forClass(UpdateProductData.Command.class);
        verify(updateProductUseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().productId()).isEqualTo(100L);
        org.assertj.core.api.Assertions.assertThat(captor.getValue().title()).isEqualTo("수정 상품");
    }

    @Test
    @DisplayName("DELETE /api/admin/shop/products/{productId} 는 상품 삭제 요청을 처리한다")
    void deleteProduct_returnsSuccessResponse() throws Exception {
        when(deleteProductUseCase.execute(any()))
                .thenReturn(new DeleteProductData.Result(100L));

        mockMvc.perform(delete("/api/admin/shop/products/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 삭제에 성공했습니다."))
                .andExpect(jsonPath("$.data.productId").value(100));

        ArgumentCaptor<DeleteProductData.Command> captor = ArgumentCaptor.forClass(DeleteProductData.Command.class);
        verify(deleteProductUseCase).execute(captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().productId()).isEqualTo(100L);
    }

    private HandlerMethodArgumentResolver authenticatedActorResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.hasParameterAnnotation(AuthenticatedActor.class);
            }

            @Override
            public Object resolveArgument(
                    MethodParameter parameter,
                    ModelAndViewContainer mavContainer,
                    org.springframework.web.context.request.NativeWebRequest webRequest,
                    WebDataBinderFactory binderFactory
            ) {
                return new Actor(1L);
            }
        };
    }
}
