package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {
    @Column(name = "business_id")
    private java.util.UUID businessId;
    private String code;
    private String name;
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "discount_value")
    private java.math.BigDecimal discountValue;
}
