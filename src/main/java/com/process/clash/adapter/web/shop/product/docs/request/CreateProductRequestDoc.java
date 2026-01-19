package com.process.clash.adapter.web.shop.product.docs.request;

import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 생성 요청")
public class CreateProductRequestDoc {

    @Schema(description = "상품명")
    public String title;

    @Schema(description = "카테고리")
    public ProductCategory category;

    @Schema(description = "이미지 URL")
    public String image;

    @Schema(description = "재화 유형")
    public ProductGoodsType type;

    @Schema(description = "가격")
    public Long price;

    @Schema(description = "할인율")
    public Integer discount;

    @Schema(description = "설명")
    public String description;

    @Schema(description = "시즌 ID (시즌 상품이 아닌 경우 null)")
    public Long seasonId;
}
