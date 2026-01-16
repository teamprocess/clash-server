package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.GetSectionsData;
import com.process.clash.application.roadmap.section.port.in.GetSectionsUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSectionsService implements GetSectionsUseCase {

    private final SectionRepositoryPort sectionRepository;

    @Override
    public GetSectionsData.Result execute(GetSectionsData.Command command) {
        // Major별 Section 조회 (orderIndex로 자동 정렬됨)
        List<Section> sections = sectionRepository.findAllByMajor(command.major());

        // Section의 category를 중복 제거하여 추출
        List<String> categories = sections.stream()
                .map(Section::getCategory)
                .distinct()
                .toList();

        // TODO: completed, locked 계산 로직 추가 필요
        // 현재는 false, false로 하드코딩
        return GetSectionsData.Result.from(sections, categories);
    }
}
