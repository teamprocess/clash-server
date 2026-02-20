package com.process.clash.application.roadmap.category.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.category.data.UpdateCategoryImageData;
import com.process.clash.application.roadmap.category.exception.exception.badrequest.InvalidCategoryImageUploadRequestException;
import com.process.clash.application.roadmap.category.exception.exception.notfound.CategoryNotFoundException;
import com.process.clash.application.roadmap.category.port.in.UpdateCategoryImageUseCase;
import com.process.clash.application.roadmap.category.port.out.CategoryImageUploadPort;
import com.process.clash.application.roadmap.category.port.out.CategoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCategoryImageService implements UpdateCategoryImageUseCase {

    private final CategoryRepositoryPort categoryRepository;
    private final CategoryImageUploadPort categoryImageUploadPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public UpdateCategoryImageData.Result execute(UpdateCategoryImageData.Command command) {
        checkAdminPolicy.check(command.actor());

        if (command.imageUrl() == null || command.imageUrl().isBlank()) {
            throw new InvalidCategoryImageUploadRequestException();
        }

        String imageUrl = command.imageUrl().trim();
        if (!categoryImageUploadPort.isValidCategoryImageUrl(command.categoryId(), imageUrl)) {
            throw new InvalidCategoryImageUploadRequestException();
        }

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        category.updateImageUrl(imageUrl);
        Category saved = categoryRepository.save(category);
        return UpdateCategoryImageData.Result.from(saved);
    }
}
