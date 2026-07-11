package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "wallets")
public class Wallet extends BaseEntity {
    @Column(name = "user_id")
    private java.util.UUID userId;
    private java.math.BigDecimal balance = java.math.BigDecimal.ZERO;
    @Column(nullable = false, columnDefinition = "char(3)")
    private String currency = "INR";
}
