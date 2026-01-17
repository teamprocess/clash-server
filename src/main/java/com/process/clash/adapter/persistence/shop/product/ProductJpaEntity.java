package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.adapter.persistence.shop.season.SeasonJpaEntity;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import com.process.clash.domain.shop.product.enums.ProductGoodsType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductCategory category;

    @Column(nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductGoodsType type;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long popularity;

    @JoinColumn(name = "fk_season_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SeasonJpaEntity season;

    @Column(nullable = false)
    private Boolean isSeasonal;
}