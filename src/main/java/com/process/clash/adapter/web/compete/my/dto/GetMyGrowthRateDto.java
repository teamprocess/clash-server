package com.process.clash.adapter.web.compete.my.dto;

import com.process.clash.application.compete.my.data.DataPoint;
import com.process.clash.application.compete.my.data.GetMyGrowthRateData;

import java.util.List;

public class GetMyGrowthRateDto {

    public record Response(
            List<DataPoint> dataPoint
    ) {

        public static Response from(GetMyGrowthRateData.Result result) {

            return new Response(
                    result.dataPoint()
            );
        }
    }
}
