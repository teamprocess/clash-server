package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.GetCategoriesData;
import com.process.clash.application.roadmap.category.port.in.GetCategoriesUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoriesService implements GetCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public GetCategoriesData.Result execute(GetCategoriesData.Command command) {
        checkAdminPolicy.check(command.actor());
        
        List<Category> categories = categoryRepository.findAll();
        return GetCategoriesData.Result.from(categories);
    }
}