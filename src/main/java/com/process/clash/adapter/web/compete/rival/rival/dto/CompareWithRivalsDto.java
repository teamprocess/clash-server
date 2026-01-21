package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.compete.rival.rival.data.CompareWithRivalsData;
import com.process.clash.application.compete.rival.rival.data.TotalData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class CompareWithRivalsDto {

    public record Response(
            String category,
            String period,
            List<TotalData> totalData
    ) {

        public static Response from(CompareWithRivalsData.Result result) {

            return new Response(
                    result.category(),
                    result.period(),
                    result.totalData()
            );
        }
    }
}
