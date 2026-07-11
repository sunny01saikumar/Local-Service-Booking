package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "customer_id")
    private java.util.UUID customerId;
    @Column(name = "business_id")
    private java.util.UUID businessId;
    private String status = "PLACED";
    @Column(name = "total_amount")
    private java.math.BigDecimal totalAmount;
    @Column(name = "payment_status")
    private String paymentStatus = "PENDING";
}
