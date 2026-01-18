package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.DeleteCategoryData;
import com.process.clash.application.roadmap.category.exception.exception.notfound.CategoryNotFoundException;
import com.process.clash.application.roadmap.category.port.in.DeleteCategoryUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public DeleteCategoryData.Result execute(DeleteCategoryData.Command command) {
        checkAdminPolicy.check(command.actor());

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException());

        categoryRepository.deleteById(command.categoryId());
        return new DeleteCategoryData.Result(command.categoryId());
    }
}