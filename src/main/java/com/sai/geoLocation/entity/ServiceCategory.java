package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "service_categories")
public class ServiceCategory extends BaseEntity {
    @Column(name = "parent_id")
    private java.util.UUID parentId;
    private String code;
    private String name;
    private String description;
    @Column(name = "display_order")
    private Integer displayOrder = 0;
}
