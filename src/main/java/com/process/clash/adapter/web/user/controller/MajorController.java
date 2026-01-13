package com.process.clash.adapter.web.user.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.adapter.web.major.mapper.MajorQuestionWebMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/major")
public class MajorController {

    @GetMapping("/questions")
    public ApiResponse<GetMajorQuestionDto.Response> getMajorQuestion() {
        return null;
    }
}
