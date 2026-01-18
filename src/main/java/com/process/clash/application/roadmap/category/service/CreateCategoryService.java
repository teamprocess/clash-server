package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.CreateCategoryData;
import com.process.clash.application.roadmap.category.exception.exception.badrequest.CategoryAlreadyExistsException;
import com.process.clash.application.roadmap.category.port.in.CreateCategoryUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCategoryService implements CreateCategoryUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public CreateCategoryData.Result execute(CreateCategoryData.Command command) {
        checkAdminPolicy.check(command.actor());

        // 이름 중복 체크
        categoryRepository.findByName(command.name())
                .ifPresent(category -> {
                    throw new CategoryAlreadyExistsException();
                });

        Category category = new Category(null, command.name(), null, null);
        Category savedCategory = categoryRepository.save(category);
        return CreateCategoryData.Result.from(savedCategory);
    }
}