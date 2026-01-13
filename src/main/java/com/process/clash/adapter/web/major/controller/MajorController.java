package com.process.clash.adapter.web.major.controller;

import com.process.clash.adapter.web.common.CommonResponse;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/major")
public class MajorController {

    @GetMapping("/questions")
    public CommonResponse<GetMajorQuestionDto.Response> getMajorQuestion() {
        return null;
    }
}
