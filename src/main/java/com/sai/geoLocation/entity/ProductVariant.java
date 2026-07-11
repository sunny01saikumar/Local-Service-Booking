package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {
    @Column(name = "product_id")
    private java.util.UUID productId;
    private String sku;
    private String barcode;
    @Column(name = "variant_name")
    private String variantName;
    private java.math.BigDecimal price;
    @Column(name = "discount_price")
    private java.math.BigDecimal discountPrice;
    private String status = "ACTIVE";
}
