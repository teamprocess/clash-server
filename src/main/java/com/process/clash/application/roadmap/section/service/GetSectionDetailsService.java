package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;
import com.process.clash.application.roadmap.section.port.in.GetSectionDetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSectionDetailsService implements GetSectionDetailsUseCase {

    private final SectionRepositoryPort sectionRepository;

    @Override
    public GetSectionDetailsData.Result execute(GetSectionDetailsData.Command command) {
        return null;
    }
}
