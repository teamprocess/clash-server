package com.process.clash.adapter.web.auth.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그아웃 성공 응답")
public class SignOutResponseDoc extends SuccessMessageResponseDoc {
}
