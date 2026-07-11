package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Column(name = "business_id")
    private java.util.UUID businessId;
    @Column(name = "category_id")
    private java.util.UUID categoryId;
    private String name;
    private String slug;
    private String brand;
    private String description;
    @Column(name = "base_price")
    private java.math.BigDecimal basePrice;
    @Column(name = "discount_price")
    private java.math.BigDecimal discountPrice;
    @Column(name = "tax_rate")
    private java.math.BigDecimal taxRate = java.math.BigDecimal.ZERO;
    private String status = "DRAFT";
}
