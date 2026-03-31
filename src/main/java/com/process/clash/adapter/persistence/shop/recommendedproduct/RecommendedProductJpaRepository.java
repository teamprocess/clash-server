package com.process.clash.adapter.persistence.shop.recommendedproduct;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedProductJpaRepository extends JpaRepository<RecommendedProductJpaEntity, Long> {
    /**
     * 이 메서드에는 비교 조건이 2개입니다. <br>
     * startDate <= ? <br>
     * endDate >= ? <br>
     * 그래서 파라미터도 2개가 필요합니다. <br>
     * 첫 번째 date: startDate <= date <br>
     * 두 번째 date: endDate >= date <br>
     * 실제로는 같은 날짜를 양쪽 비교에 넣고 싶어서 호출할 때 둘 다 같은 값을 넘긴 겁니다. <br>
     * 따라서 startDateBoundary == endDateBoundary <br>
     * @param startDateBoundary 현재 날짜(=endDateBoundary)
     * @param endDateBoundary 현재 날짜(=startDateBoundary)
     * @return 활성화이면서 날짜까지 충족한 10개 추천 상품
     * @author Finefinee
     */
    List<RecommendedProductJpaEntity> findTop10ByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByDisplayOrderAsc(
            LocalDate startDateBoundary,
            LocalDate endDateBoundary
    );
}
