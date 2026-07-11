package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {
    @Column(name = "cart_id")
    private java.util.UUID cartId;
    @Column(name = "business_id")
    private java.util.UUID businessId;
    @Column(name = "product_variant_id")
    private java.util.UUID productVariantId;
    @Column(name = "service_id")
    private java.util.UUID serviceId;
    private Integer quantity = 1;
    @Column(name = "unit_price")
    private java.math.BigDecimal unitPrice;
}
