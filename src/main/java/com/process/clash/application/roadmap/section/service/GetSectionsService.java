package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.common.SectionLockedBooleanClassifier;
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
    private final SectionLockedBooleanClassifier sectionLockedBooleanClassifier;

    @Override
    public GetSectionsData.Result execute(GetSectionsData.Command command) {
        // Major별 Section 조회 (orderIndex로 자동 정렬됨)
        List<Section> sections = sectionRepository.findAllByMajor(command.major());

        // Section의 category를 중복 제거하여 추출
        List<String> categories = sections.stream()
                .map(Section::getCategory)
                .distinct()
                .toList();

        // 각 Section에 대해 locked 여부 판단
        List<GetSectionsData.Result.SectionVo> sectionVos = sections.stream()
                .map(section -> {
                    boolean locked = sectionLockedBooleanClassifier.check(command.actor(), section);
                    // TODO: completed 계산 로직 추가 필요
                    return GetSectionsData.Result.SectionVo.from(section, false, locked);
                })
                .toList();

        return new GetSectionsData.Result(sectionVos, categories);
    }
}
