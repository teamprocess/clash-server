package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.roadmap.category.data.GetCategoriesData;
import com.process.clash.application.roadmap.category.port.in.GetCategoriesUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoriesService implements GetCategoriesUseCase {

    private final CategoryRepositoryPort categoryRepository;

    @Override
    public GetCategoriesData.Result execute(GetCategoriesData.Command command) {
        List<com.process.clash.domain.roadmap.entity.Category> categories = categoryRepository.findAll();
        return GetCategoriesData.Result.from(categories);
    }
}