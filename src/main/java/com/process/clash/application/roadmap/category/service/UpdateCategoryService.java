package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.UpdateCategoryData;
import com.process.clash.application.roadmap.category.exception.exception.badrequest.CategoryAlreadyExistsException;
import com.process.clash.application.roadmap.category.exception.exception.notfound.CategoryNotFoundException;
import com.process.clash.application.roadmap.category.port.in.UpdateCategoryUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCategoryService implements UpdateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public UpdateCategoryData.Result execute(UpdateCategoryData.Command command) {
        checkAdminPolicy.check(command.actor());

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        // 이름 변경 시 중복 체크
        if (!category.getName().equals(command.name())) {
            categoryRepository.findByName(command.name())
                    .ifPresent(existingCategory -> {
                        throw new CategoryAlreadyExistsException();
                    });
        }

        category.updateName(command.name());
        Category savedCategory = categoryRepository.save(category);
        return UpdateCategoryData.Result.from(savedCategory);
    }
}