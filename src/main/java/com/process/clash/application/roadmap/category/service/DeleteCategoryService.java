package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.DeleteCategoryData;
import com.process.clash.application.roadmap.category.exception.exception.badrequest.CategoryInUseException;
import com.process.clash.application.roadmap.category.exception.exception.notfound.CategoryNotFoundException;
import com.process.clash.application.roadmap.category.port.in.DeleteCategoryUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final SectionRepositoryPort sectionRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public DeleteCategoryData.Result execute(DeleteCategoryData.Command command) {
        checkAdminPolicy.check(command.actor());

        // 카테고리 존재 여부 확인
        if (!categoryRepository.existsById(command.categoryId())) {
            throw new CategoryNotFoundException();
        }

        // 해당 카테고리를 사용하는 Section이 있는지 확인
        if (sectionRepository.existsByCategoryId(command.categoryId())) {
            throw new CategoryInUseException();
        }

        categoryRepository.deleteById(command.categoryId());
        return new DeleteCategoryData.Result(command.categoryId());
    }
}