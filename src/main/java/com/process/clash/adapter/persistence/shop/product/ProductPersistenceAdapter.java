package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.application.shop.product.port.out.ProductRepositoryPort;
import com.process.clash.domain.shop.product.entity.Product;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductJpaMapper productJpaMapper;

    @Override
    public Product save(Product product) {
        ProductJpaEntity productJpaEntity = productJpaMapper.toJpaEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(productJpaEntity);
        return productJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId)
                .map(productJpaMapper::toDomain);
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> productIds) {
        return productJpaRepository.findByIdIn(productIds)
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findTop10ByOrderByPopularityDesc() {
        return productJpaRepository.findTop10ByOrderByPopularityDesc()
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult findAllByPage(Integer page, Integer size, ProductSortType sort, ProductCategory category) {
        Pageable pageable = createPageable(page, size, sort);
        Page<ProductJpaEntity> pageResult = findAllBySortAndCategory(sort, category, pageable);
        return toPageResult(pageResult);
    }

    @Override
    public PageResult searchByKeywordByPage(
            Integer page,
            Integer size,
            ProductSortType sort,
            ProductCategory category,
            String keyword
    ) {
        Pageable pageable = createPageable(page, size, sort);
        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        Page<ProductJpaEntity> pageResult = normalizedKeyword.isEmpty()
                ? findAllBySortAndCategory(sort, category, pageable)
                : searchBySortCategoryAndKeyword(sort, category, normalizedKeyword, pageable);

        return toPageResult(pageResult);
    }

    private Sort createSort(ProductSortType sortType) {
        return switch (sortType) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            case POPULAR -> Sort.by(Sort.Direction.DESC, "popularity");
            case EXPENSIVE -> Sort.by(Sort.Direction.DESC, "price");
            case CHEAPEST -> Sort.by(Sort.Direction.ASC, "price");
            case DISCOUNT -> Sort.by(Sort.Direction.DESC, "discount")
                    .and(Sort.by(Sort.Direction.DESC, "createdAt"));
        };
    }

    private boolean isDiscountSort(ProductSortType sortType) {
        return sortType == ProductSortType.DISCOUNT;
    }

    private Pageable createPageable(Integer page, Integer size, ProductSortType sortType) {
        return PageRequest.of(page - 1, size, createSort(sortType));
    }

    private Page<ProductJpaEntity> findAllBySortAndCategory(
            ProductSortType sortType,
            ProductCategory category,
            Pageable pageable
    ) {
        if (isDiscountSort(sortType)) {
            return category == null
                    ? productJpaRepository.findByDiscountGreaterThan(0, pageable)
                    : productJpaRepository.findByCategoryAndDiscountGreaterThan(category, 0, pageable);
        }

        return category == null
                ? productJpaRepository.findAll(pageable)
                : productJpaRepository.findByCategory(category, pageable);
    }

    private Page<ProductJpaEntity> searchBySortCategoryAndKeyword(
            ProductSortType sortType,
            ProductCategory category,
            String keyword,
            Pageable pageable
    ) {
        if (isDiscountSort(sortType)) {
            return category == null
                    ? productJpaRepository.searchDiscountedByKeyword(keyword, pageable)
                    : productJpaRepository.searchDiscountedByCategoryAndKeyword(category, keyword, pageable);
        }

        return category == null
                ? productJpaRepository.searchByKeyword(keyword, pageable)
                : productJpaRepository.searchByCategoryAndKeyword(category, keyword, pageable);
    }

    private PageResult toPageResult(Page<ProductJpaEntity> pageResult) {
        List<Product> products = pageResult.getContent()
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();
        return new PageResult(products, pageResult.getTotalElements());
    }
}
