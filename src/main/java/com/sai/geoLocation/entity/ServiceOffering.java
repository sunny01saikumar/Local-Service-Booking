package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "services")
public class ServiceOffering extends BaseEntity {
    @Column(name = "business_id")
    private java.util.UUID businessId;
    @Column(name = "category_id")
    private java.util.UUID categoryId;
    private String name;
    private String slug;
    private String description;
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    private java.math.BigDecimal price;
    @Column(name = "discount_price")
    private java.math.BigDecimal discountPrice;
    @Column(name = "home_visit")
    private boolean homeVisit;
    @Column(name = "in_shop_service")
    private boolean inShopService = true;
    private String status = "DRAFT";
}
