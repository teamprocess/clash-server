package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "보유 아이템 데이터")
public class GetMyItemsDataDocument {

    @ArraySchema(schema = @Schema(implementation = GetMyItemDocument.class))
    public List<GetMyItemDocument> items;
}
