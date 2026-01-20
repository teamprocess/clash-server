package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.compete.rival.data.TotalData;

import java.util.List;

public class CompareWithRivalsDto {

    public record Response(
            String category,
            String period,
            List<TotalData> totalData
    ) {}
}
