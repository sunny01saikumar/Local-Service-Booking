package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity {
    @Column(name = "business_location_id")
    private java.util.UUID businessLocationId;
    @Column(name = "product_variant_id")
    private java.util.UUID productVariantId;
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;
}
