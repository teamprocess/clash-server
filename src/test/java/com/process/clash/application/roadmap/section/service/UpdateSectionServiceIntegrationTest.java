package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.CreateCategoryData;
import com.process.clash.application.roadmap.category.port.in.CreateCategoryUseCase;
import com.process.clash.application.roadmap.section.data.CreateSectionData;
import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.application.roadmap.section.port.in.CreateSectionUseCase;
import com.process.clash.application.roadmap.section.port.in.UpdateSectionUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionKeyPointRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.Role;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UpdateSectionService의 keyPoints 업데이트 시 JPA Cascade와의 충돌 여부를 확인하는 통합 테스트
 *
 * 테스트 목적:
 * 1. orphanRemoval = true와 명시적 bulk delete의 충돌 확인
 * 2. 중복 DELETE 쿼리 발생 여부 확인
 * 3. 데이터 일관성 확인
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UpdateSectionServiceIntegrationTest {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CreateSectionUseCase createSectionUseCase;

    @Autowired
    private UpdateSectionUseCase updateSectionUseCase;

    @Autowired
    private SectionKeyPointRepositoryPort keyPointRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Statistics statistics;
    private Actor adminActor;

    @BeforeEach
    void setUp() {
        // Hibernate Statistics 활성화 (쿼리 수 측정용)
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();

        // 관리자 Actor 생성
        adminActor = new Actor(1L, Role.ADMIN);
    }

    @Test
    @DisplayName("KeyPoints 업데이트 시 orphanRemoval과 명시적 bulk delete가 충돌하지 않는지 확인")
    void updateKeyPoints_shouldNotConflictWithOrphanRemoval() {
        // Given: Category 생성
        CreateCategoryData.Result categoryResult = createCategoryUseCase.execute(
                new CreateCategoryData.Command(adminActor, "BASIC")
        );

        // Given: Section 생성 (keyPoints 3개)
        CreateSectionData.Command createCommand = new CreateSectionData.Command(
                adminActor,
                Major.SERVER,
                "Test Section",
                categoryResult.categoryId(),
                "Test Description",
                List.of("point1", "point2", "point3")
        );
        CreateSectionData.Result createResult = createSectionUseCase.execute(createCommand);
        Long sectionId = createResult.sectionId();

        // 트랜잭션 커밋 및 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
        statistics.clear();

        // When: keyPoints를 2개로 업데이트 (기존 3개 삭제 + 새로운 2개 삽입)
        UpdateSectionData.Command updateCommand = new UpdateSectionData.Command(
                adminActor,
                sectionId,
                null,  // title 변경 안 함
                null,  // category 변경 안 함
                null,  // description 변경 안 함
                null,  // orderIndex 변경 안 함
                List.of("new1", "new2"),  // keyPoints만 변경
                null   // prerequisites 변경 안 함
        );
        UpdateSectionData.Result updateResult = updateSectionUseCase.execute(updateCommand);

        entityManager.flush();
        entityManager.clear();

        // Then: 결과 검증
        // 1. Result에 반환된 keyPoints가 2개인지 확인
        assertThat(updateResult.keyPoints())
                .hasSize(2)
                .containsExactly("new1", "new2");

        // 2. 실제 DB에 저장된 keyPoints 확인
        List<SectionKeyPoint> savedKeyPoints = keyPointRepository.findAllBySectionId(sectionId);
        assertThat(savedKeyPoints).hasSize(2);
        assertThat(savedKeyPoints)
                .extracting(SectionKeyPoint::getContent)
                .containsExactly("new1", "new2");

        // 3. orderIndex가 올바르게 설정되었는지 확인
        assertThat(savedKeyPoints.get(0).getOrderIndex()).isEqualTo(0);
        assertThat(savedKeyPoints.get(1).getOrderIndex()).isEqualTo(1);

        // 4. Hibernate Statistics 출력 (수동 분석용)
        System.out.println("=== Hibernate Statistics ===");
        System.out.println("Entity Delete Count: " + statistics.getEntityDeleteCount());
        System.out.println("Entity Insert Count: " + statistics.getEntityInsertCount());
        System.out.println("Entity Update Count: " + statistics.getEntityUpdateCount());
        System.out.println("Query Execution Count: " + statistics.getQueryExecutionCount());
        System.out.println("===========================");
    }

    @Test
    @DisplayName("KeyPoints를 빈 리스트로 업데이트하면 모든 keyPoints가 삭제되는지 확인")
    void updateKeyPoints_withEmptyList_shouldDeleteAllKeyPoints() {
        // Given: Category 생성
        CreateCategoryData.Result categoryResult = createCategoryUseCase.execute(
                new CreateCategoryData.Command(adminActor, "BASIC")
        );

        // Given: Section 생성 (keyPoints 3개)
        CreateSectionData.Command createCommand = new CreateSectionData.Command(
                adminActor,
                Major.SERVER,
                "Test Section",
                categoryResult.categoryId(),
                "Test Description",
                List.of("point1", "point2", "point3")
        );
        CreateSectionData.Result createResult = createSectionUseCase.execute(createCommand);
        Long sectionId = createResult.sectionId();

        entityManager.flush();
        entityManager.clear();

        // When: keyPoints를 빈 리스트로 업데이트
        UpdateSectionData.Command updateCommand = new UpdateSectionData.Command(
                adminActor,
                sectionId,
                null, null, null, null,
                List.of(),  // 빈 리스트
                null
        );
        UpdateSectionData.Result updateResult = updateSectionUseCase.execute(updateCommand);

        entityManager.flush();
        entityManager.clear();

        // Then: 모든 keyPoints가 삭제되었는지 확인
        List<SectionKeyPoint> savedKeyPoints = keyPointRepository.findAllBySectionId(sectionId);
        assertThat(savedKeyPoints).isEmpty();
        assertThat(updateResult.keyPoints()).isEmpty();
    }

    @Test
    @DisplayName("KeyPoints를 null로 업데이트하면 기존 keyPoints가 유지되는지 확인")
    void updateKeyPoints_withNull_shouldKeepExistingKeyPoints() {
        // Given: Category 생성
        CreateCategoryData.Result categoryResult = createCategoryUseCase.execute(
                new CreateCategoryData.Command(adminActor, "BASIC")
        );

        // Given: Section 생성 (keyPoints 3개)
        CreateSectionData.Command createCommand = new CreateSectionData.Command(
                adminActor,
                Major.SERVER,
                "Test Section",
                categoryResult.categoryId(),
                "Test Description",
                List.of("point1", "point2", "point3")
        );
        CreateSectionData.Result createResult = createSectionUseCase.execute(createCommand);
        Long sectionId = createResult.sectionId();

        entityManager.flush();
        entityManager.clear();

        // When: keyPoints를 null로 업데이트 (title만 변경)
        UpdateSectionData.Command updateCommand = new UpdateSectionData.Command(
                adminActor,
                sectionId,
                "Updated Title",
                null, null, null,
                null,  // keyPoints 수정 안 함
                null
        );
        UpdateSectionData.Result updateResult = updateSectionUseCase.execute(updateCommand);

        entityManager.flush();
        entityManager.clear();

        // Then: 기존 keyPoints가 유지되는지 확인
        List<SectionKeyPoint> savedKeyPoints = keyPointRepository.findAllBySectionId(sectionId);
        assertThat(savedKeyPoints).hasSize(3);
        assertThat(savedKeyPoints)
                .extracting(SectionKeyPoint::getContent)
                .containsExactly("point1", "point2", "point3");

        // Result에서도 기존 keyPoints를 반환하는지 확인
        assertThat(updateResult.keyPoints()).hasSize(3);
    }

    @Test
    @DisplayName("KeyPoints 대량 업데이트 시 성능 확인 (10개 -> 10개)")
    void updateKeyPoints_bulkUpdate_performanceTest() {
        // Given: Category 생성
        CreateCategoryData.Result categoryResult = createCategoryUseCase.execute(
                new CreateCategoryData.Command(adminActor, "BASIC")
        );

        // Given: Section 생성 (keyPoints 10개)
        List<String> initialKeyPoints = List.of("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10");
        CreateSectionData.Command createCommand = new CreateSectionData.Command(
                adminActor,
                Major.SERVER,
                "Test Section",
                categoryResult.categoryId(),
                "Test Description",
                initialKeyPoints
        );
        CreateSectionData.Result createResult = createSectionUseCase.execute(createCommand);
        Long sectionId = createResult.sectionId();

        entityManager.flush();
        entityManager.clear();
        statistics.clear();

        // When: 완전히 다른 10개로 교체
        List<String> newKeyPoints = List.of("new1", "new2", "new3", "new4", "new5", "new6", "new7", "new8", "new9", "new10");
        UpdateSectionData.Command updateCommand = new UpdateSectionData.Command(
                adminActor,
                sectionId,
                null, null, null, null,
                newKeyPoints,
                null
        );
        updateSectionUseCase.execute(updateCommand);

        entityManager.flush();

        // Then: 쿼리 수 확인
        System.out.println("=== Bulk Update Performance (10 -> 10) ===");
        System.out.println("Entity Delete Count: " + statistics.getEntityDeleteCount());
        System.out.println("Entity Insert Count: " + statistics.getEntityInsertCount());
        System.out.println("Query Execution Count: " + statistics.getQueryExecutionCount());
        System.out.println("==========================================");

        // 실제 데이터 검증
        entityManager.clear();
        List<SectionKeyPoint> savedKeyPoints = keyPointRepository.findAllBySectionId(sectionId);
        assertThat(savedKeyPoints).hasSize(10);
        assertThat(savedKeyPoints)
                .extracting(SectionKeyPoint::getContent)
                .containsExactlyElementsOf(newKeyPoints);
    }
}
