package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaMapper;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaEntity;
import com.process.clash.adapter.persistence.roadmap.keypoint.SectionKeyPointJpaMapper;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.Chapter;        // Domain Chapter import
import com.process.clash.domain.roadmap.entity.SectionKeyPoint; // Domain KeyPoint import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SectionPersistenceAdapter implements SectionRepositoryPort {
    private final SectionJpaRepository sectionJpaRepository;
    private final SectionJpaMapper sectionJpaMapper;
    private final ChapterJpaMapper chapterJpaMapper;
    private final SectionKeyPointJpaMapper sectionKeyPointJpaMapper;

    @Override
    public Section save(Section section) {
        if (section.getId() == null) {
            // 생성: 완전 신규 생성 시에는 Mapper가 만든 객체를 바로 저장해도 됨 (ID가 없으므로)
            SectionJpaEntity newEntity = sectionJpaMapper.toJpaEntity(section);
            SectionJpaEntity saved = sectionJpaRepository.save(newEntity);
            return sectionJpaMapper.toDomain(saved);
        }

        // [UPDATE] 조회 -> 변경 -> Flush
        SectionJpaEntity entity = sectionJpaRepository.findById(section.getId())
                .orElseThrow(SectionNotFoundException::new);

        updateSectionDetails(entity, section);

        sectionJpaRepository.flush(); // 즉시 DB 반영 -> updatedAt 갱신 용도

        return sectionJpaMapper.toDomain(entity);
    }

    @Override
    public List<Section> saveAll(List<Section> sections) {
        if (sections == null || sections.isEmpty()) {
            return List.of();
        }

        // 1. ID 추출 (수정 대상 식별)
        List<Long> ids = sections.stream()
                .map(Section::getId)
                .filter(Objects::nonNull)
                .toList();

        // 2. Bulk Fetch (성능 최적화)
        Map<Long, SectionJpaEntity> entityMap = sectionJpaRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(SectionJpaEntity::getId, e -> e));

        List<SectionJpaEntity> resultEntities = new ArrayList<>();

        for (Section domain : sections) {
            if (domain.getId() != null && entityMap.containsKey(domain.getId())) {
                // [UPDATE] 기존 영속 객체를 가져와서 동기화
                SectionJpaEntity entity = entityMap.get(domain.getId());
                updateSectionDetails(entity, domain);
                resultEntities.add(entity);
            } else {
                // [CREATE] 신규 객체는 바로 생성
                SectionJpaEntity newEntity = sectionJpaMapper.toJpaEntity(domain);
                resultEntities.add(newEntity);
            }
        }

        // 3. 저장 및 Flush
        List<SectionJpaEntity> savedEntities = sectionJpaRepository.saveAll(resultEntities);
        sectionJpaRepository.flush();

        return savedEntities.stream()
                .map(sectionJpaMapper::toDomain)
                .toList();
    }

    // ... findById, findAll 등 조회 메서드는 기존과 동일하므로 생략 가능 ...
    @Override
    public Optional<Section> findById(Long id) {
        return sectionJpaRepository.findById(id).map(sectionJpaMapper::toDomain);
    }

    @Override
    public List<Section> findAllById(List<Long> ids) {
        return sectionJpaRepository.findAllById(ids).stream().map(sectionJpaMapper::toDomain).toList();
    }

    @Override
    public List<Section> findAll() {
        return sectionJpaRepository.findAll().stream().map(sectionJpaMapper::toDomain).toList();
    }

    @Override
    public List<Section> findAllByMajor(Major major) {
        return sectionJpaRepository.findAllByMajorOrderByOrderIndexAsc(major).stream().map(sectionJpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        sectionJpaRepository.deleteById(id);
    }

    /*
    1. 엔티티를 도메인에 맞춰 업데이트
    2. 챕터가 있다면 원래 있던 챕터에서 요청에 없는 챕터 제거
    3. 추가 혹은 수정
    4. 키포인트, 선수 로드맵도 비슷하게 진행
     */
    private void updateSectionDetails(SectionJpaEntity entity, Section domain) {
        // 1. 기본 필드 업데이트
        entity.updateFields(
                domain.getMajor(),
                domain.getTitle(),
                domain.getCategory(),
                domain.getDescription(),
                domain.getOrderIndex()
        );

        // 2. Chapters 리스트 동기화
        if (domain.getChapters() != null) {
            List<ChapterJpaEntity> existingChapters = entity.getChapters();
            List<Chapter> incomingChapters = domain.getChapters(); // Domain 객체 리스트

            // 요청에 없는 ID는 삭제
            Set<Long> incomingIds = incomingChapters.stream()
                    .map(Chapter::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // removeIf를 사용하면 orphanRemoval=true에 의해 DB에서도 삭제됨
            existingChapters.removeIf(ch -> ch.getId() != null && !incomingIds.contains(ch.getId()));

            // 추가 혹은 수정
            for (Chapter incoming : incomingChapters) {
                if (incoming.getId() == null) {
                    // 추가
                    existingChapters.add(chapterJpaMapper.toEntity(incoming, entity));
                } else {
                    // 수정
                    existingChapters.stream()
                            .filter(ch -> ch.getId().equals(incoming.getId()))
                            .findFirst()
                            .ifPresent(ch -> {
                                // TODO: ChapterJpaEntity에도 update() 메서드를 만들어주세요!
                                // 예: ch.update(incoming.getTitle(), incoming.getVideoUrl(), ...);
                                // 임시 코드:
                                // ch.setTitle(incoming.getTitle());
                            });
                }
            }
        }

        // 3. KeyPoints 리스트 동기화
        if (domain.getKeyPoints() != null) {

            entity.getKeyPoints().clear();

            List<SectionKeyPointJpaEntity> newKeyPoints = domain.getKeyPoints().stream()
                    .map(kp -> new SectionKeyPointJpaEntity(
                            null,             // ID를 강제로 null로 설정 (그래야 INSERT 쿼리가 나감)
                            entity,           // 부모(Section) 연결
                            kp.getContent(),  // 내용
                            kp.getOrderIndex() != null ? kp.getOrderIndex() : 0, // 순서
                            null,             // createdAt (JPA 자동 생성)
                            null              // updatedAt (JPA 자동 생성)
                    ))
                    .toList();

            entity.getKeyPoints().addAll(newKeyPoints);
        }

        // 4. Prerequisites (선수 로드맵) 교체
        // ManyToMany는 관계 테이블만 관리하므로, 보통 ID 조회 후 Set 교체 방식을 써도 무방함
        if (domain.getPrerequisites() != null) {
            List<Long> prereqIds = domain.getPrerequisites().stream()
                    .map(Section::getId)
                    .toList();

            if (!prereqIds.isEmpty()) {
                // DB에서 실제 엔티티를 조회하여 영속성 컨텍스트가 관리하는 객체로 세팅
                Set<SectionJpaEntity> managedPrereqs = new HashSet<>(sectionJpaRepository.findAllById(prereqIds));
                entity.updatePrerequisites(managedPrereqs);
            } else {
                entity.updatePrerequisites(new HashSet<>());
            }
        }
    }
}