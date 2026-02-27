package com.process.clash.adapter.persistence.shop.product;

import com.process.clash.adapter.persistence.shop.season.SeasonJpaEntity;
import com.process.clash.domain.shop.product.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.persistence.EntityListeners;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductCategory category;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long popularity;

    @JoinColumn(name = "fk_season_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SeasonJpaEntity season;

    @Column(nullable = false)
    private Boolean isSeasonal;
}
