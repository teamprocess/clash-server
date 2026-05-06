package com.process.clash.adapter.web.shop.product.docs.request;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 생성 요청")
public class CreateProductRequestDocument {

    @Schema(description = "상품명")
    public String title;

    @Schema(description = "카테고리")
    public ProductCategory category;

    @Schema(description = "이미지 URL")
    public String image;

    @Schema(description = "오디오 URL (BGM 카테고리 전용, 그 외 null)")
    public String audio;

    @Schema(description = "가격")
    public Long price;

    @Schema(description = "할인율")
    public Integer discount;

    @Schema(description = "설명")
    public String description;

    @Schema(description = "시즌 ID (시즌 상품이 아닌 경우 null)")
    public Long seasonId;

    @Schema(description = "구매 가능 여부 (기본값: true)", defaultValue = "true")
    public Boolean isAblePurchase;
}
