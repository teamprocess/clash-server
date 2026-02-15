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
        Sort sortCondition = createSort(sort);
        Pageable pageable = PageRequest.of(page - 1, size, sortCondition);

        Page<ProductJpaEntity> pageResult;
        if (category == null) {
            pageResult = productJpaRepository.findAll(pageable);
        } else {
            pageResult = productJpaRepository.findByCategory(category, pageable);
        }

        List<Product> products = pageResult.getContent()
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();

        return new PageResult(products, pageResult.getTotalElements());
    }

    @Override
    public PageResult searchByKeywordByPage(
            Integer page,
            Integer size,
            ProductSortType sort,
            ProductCategory category,
            String keyword
    ) {
        Sort sortCondition = createSort(sort);
        Pageable pageable = PageRequest.of(page - 1, size, sortCondition);
        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        Page<ProductJpaEntity> pageResult;
        if (normalizedKeyword.isEmpty()) {
            if (category == null) {
                pageResult = productJpaRepository.findAll(pageable);
            } else {
                pageResult = productJpaRepository.findByCategory(category, pageable);
            }
        } else {
            if (category == null) {
                pageResult = productJpaRepository.searchByKeyword(normalizedKeyword, pageable);
            } else {
                pageResult = productJpaRepository.searchByCategoryAndKeyword(category, normalizedKeyword, pageable);
            }
        }

        List<Product> products = pageResult.getContent()
                .stream()
                .map(productJpaMapper::toDomain)
                .toList();

        return new PageResult(products, pageResult.getTotalElements());
    }

    private Sort createSort(ProductSortType sortType) {
        return switch (sortType) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            case POPULAR -> Sort.by(Sort.Direction.DESC, "popularity");
            case EXPENSIVE -> Sort.by(Sort.Direction.DESC, "price");
            case CHEAPEST -> Sort.by(Sort.Direction.ASC, "price");
        };
    }
}
