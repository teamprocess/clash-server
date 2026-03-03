package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "보유한 아이템 목록 조회 응답",
        example = """
            {
              "success": true,
              "message": "소유한 아이템 목록을 성공적으로 조회했습니다.",
              "data": {
                "items": [
                  {
                    "id": 1,
                    "title": "기본 인시그니아",
                    "image": "https://cdn.example.com/items/insignia.png",
                    "description": "기본 아이템",
                    "category": "INSIGNIA",
                    "price": 100,
                    "discount": 0,
                    "popularity": 10,
                    "isSeasonal": false
                  }
                ]
              }
            }
            """
)
public class GetMyItemsResponseDocument extends SuccessResponseDocument {

    @Schema(description = "보유 아이템")
    public GetMyItemsDataDocument data;
}
