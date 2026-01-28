package com.process.clash.adapter.web.group.util;

import com.process.clash.application.group.exception.exception.badrequest.InvalidCategoryException;
import com.process.clash.domain.group.enums.GroupCategory;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class GroupCategoryConverter {

    private GroupCategoryConverter() {
        // Utility class
    }

    /**
     * 문자열 카테고리를 GroupCategory enum으로 변환합니다.
     *
     * @param category 카테고리 문자열 (null, 빈 문자열, "ALL"은 null 반환)
     * @return GroupCategory enum 또는 null
     * @throws InvalidCategoryException 유효하지 않은 카테고리인 경우
     */
    public static GroupCategory toCategoryFilter(String category) {
        if (category == null || category.isBlank() || "ALL".equalsIgnoreCase(category)) {
            return null;
        }

        try {
            return GroupCategory.valueOf(category.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            String validCategories = Arrays.stream(GroupCategory.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            String errorMessage = String.format(
                    "유효하지 않은 카테고리입니다: '%s'. 사용 가능한 카테고리: %s",
                    category,
                    validCategories
            );
            throw new InvalidCategoryException(errorMessage);
        }
    }
}
